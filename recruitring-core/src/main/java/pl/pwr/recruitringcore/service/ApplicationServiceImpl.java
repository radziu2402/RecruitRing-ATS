package pl.pwr.recruitringcore.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pwr.recruitringcore.blobstorage.AzureBlobStorageService;
import pl.pwr.recruitringcore.dto.ApplicationDTO;
import pl.pwr.recruitringcore.dto.CandidateDTO;
import pl.pwr.recruitringcore.model.entities.Application;
import pl.pwr.recruitringcore.model.entities.Candidate;
import pl.pwr.recruitringcore.model.entities.Document;
import pl.pwr.recruitringcore.model.entities.JobPosting;
import pl.pwr.recruitringcore.model.enums.ApplicationStatus;
import pl.pwr.recruitringcore.repo.ApplicationRepository;
import pl.pwr.recruitringcore.repo.CandidateRepository;
import pl.pwr.recruitringcore.repo.JobRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final CandidateRepository candidateRepository;
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobPostingRepository;
    private final AzureBlobStorageService blobStorageService;

    public ApplicationServiceImpl(CandidateRepository candidateRepository, ApplicationRepository applicationRepository,
                                  JobRepository jobPostingRepository, AzureBlobStorageService blobStorageService) {
        this.candidateRepository = candidateRepository;
        this.applicationRepository = applicationRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.blobStorageService = blobStorageService;
    }

    @Transactional
    @Override
    public ResponseEntity<String> processApplication(ApplicationDTO applicationDto, MultipartFile cvFile) {
        Optional<Candidate> optionalCandidate = candidateRepository.findByEmail(applicationDto.getEmail());
        Candidate candidate;

        if (optionalCandidate.isPresent()) {
            candidate = optionalCandidate.get();
        } else {
            candidate = Candidate.builder()
                    .firstName(applicationDto.getFirstName())
                    .lastName(applicationDto.getLastName())
                    .email(applicationDto.getEmail())
                    .phone(applicationDto.getPhone())
                    .address(applicationDto.getAddress())
                    .build();

            candidateRepository.save(candidate);
        }

        Optional<JobPosting> optionalJobPosting = jobPostingRepository.findByOfferCode(applicationDto.getOfferCode());

        if (optionalJobPosting.isEmpty()) {
            return ResponseEntity.badRequest().body("Oferta pracy nie została znaleziona.");
        }

        String cvFileName;
        try {
            ResponseEntity<String> response = blobStorageService.upload(cvFile, applicationDto.getEmail() + "_CV.pdf");

            if (response.getStatusCode().is2xxSuccessful()) {
                cvFileName = response.getBody();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas zapisywania CV.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas przesyłania pliku: " + e.getMessage());
        }

        Application application = Application.builder()
                .candidate(candidate)
                .jobPosting(optionalJobPosting.get())
                .appliedAt(LocalDateTime.now())
                .status(ApplicationStatus.NEW)
                .build();

        Document cvDocument = Document.builder()
                .application(application)
                .candidate(candidate)
                .fileName(cvFileName)
                .fileType("PDF")
                .uploadedAt(LocalDateTime.now())
                .build();

        if (application.getDocuments() == null) {
            application.setDocuments(new HashSet<>());
        }

        application.getDocuments().add(cvDocument);

        applicationRepository.save(application);

        return ResponseEntity.ok().build();
    }

    @Override
    public List<CandidateDTO> getCandidatesByOfferCode(String offerCode) {
        List<Application> applications = applicationRepository.findByJobPostingOfferCode(UUID.fromString(offerCode));
        return applications.stream()
                .map(application -> new CandidateDTO(
                        application.getCandidate().getFirstName(),
                        application.getCandidate().getLastName(),
                        application.getCandidate().getEmail(),
                        application.getCandidate().getPhone(),
                        application.getStatus().toString()
                ))
                .toList();
    }

}
