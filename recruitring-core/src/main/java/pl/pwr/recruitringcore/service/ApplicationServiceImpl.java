package pl.pwr.recruitringcore.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.pwr.recruitringcore.blobstorage.AzureBlobStorageService;
import pl.pwr.recruitringcore.dto.*;
import pl.pwr.recruitringcore.model.entities.*;
import pl.pwr.recruitringcore.model.enums.ApplicationStatus;
import pl.pwr.recruitringcore.repo.ApplicationRepository;
import pl.pwr.recruitringcore.repo.CandidateRepository;
import pl.pwr.recruitringcore.repo.JobRepository;
import pl.pwr.recruitringcore.repo.NoteRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final CandidateRepository candidateRepository;
    private final ApplicationRepository applicationRepository;
    private final NoteRepository noteRepository;
    private final JobRepository jobPostingRepository;
    private final AzureBlobStorageService blobStorageService;

    public ApplicationServiceImpl(CandidateRepository candidateRepository, ApplicationRepository applicationRepository, NoteRepository noteRepository,
                                  JobRepository jobPostingRepository, AzureBlobStorageService blobStorageService) {
        this.candidateRepository = candidateRepository;
        this.applicationRepository = applicationRepository;
        this.noteRepository = noteRepository;
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
                .sorted(Comparator.comparing(application -> application.getCandidate().getId()))
                .map(application -> new CandidateDTO(
                        application.getApplicationCode(),
                        application.getCandidate().getFirstName(),
                        application.getCandidate().getLastName(),
                        application.getCandidate().getEmail(),
                        application.getCandidate().getPhone(),
                        application.getStatus().toString(),
                        application.getRating(),
                        application.getCandidate().getAddress().getCity()
                ))
                .toList();
    }

    @Override
    public DetailedCandidateDTO getCandidateDetails(UUID applicationCode) {
        return applicationRepository.findByApplicationCode(applicationCode)
                .map(application -> DetailedCandidateDTO.builder()
                        .applicationCode(application.getApplicationCode())
                        .firstName(application.getCandidate().getFirstName())
                        .lastName(application.getCandidate().getLastName())
                        .email(application.getCandidate().getEmail())
                        .phone(application.getCandidate().getPhone())
                        .status(application.getStatus().toString())
                        .rating(application.getRating())
                        .city(application.getCandidate().getAddress().getCity())
                        .notes(application.getNotes().stream()
                                .sorted(Comparator.comparing(Note::getId))
                                .map(note -> NoteDTO.builder()
                                        .id(note.getId())
                                        .content(note.getContent())
                                        .createdAt(note.getCreatedAt())
                                        .build())
                                .toList())
                        .documents(application.getDocuments().stream()
                                .map(document -> DocumentDTO.builder()
                                        .id(document.getId())
                                        .fileName(document.getFileName())
                                        .fileType(document.getFileType())
                                        .uploadedAt(document.getUploadedAt())
                                        .build())
                                .toList())
                        .build())
                .orElse(null);
    }

    @Transactional
    @Override
    public boolean updateCandidate(UUID applicationCode, DetailedCandidateDTO candidateDto) {
        Optional<Application> optionalApplication = applicationRepository.findByApplicationCode(applicationCode);

        if (optionalApplication.isPresent()) {
            Application application = optionalApplication.get();
            application.setRating(candidateDto.getRating());
            application.setStatus(ApplicationStatus.valueOf(candidateDto.getStatus()));

            Map<Long, Note> existingNotesMap = application.getNotes().stream()
                    .collect(Collectors.toMap(Note::getId, Function.identity()));

            Set<Note> updatedNotes = new HashSet<>();

            candidateDto.getNotes().forEach(noteDto -> {
                Note note = existingNotesMap.getOrDefault(noteDto.getId(), new Note());
                note.setApplication(application);
                note.setContent(noteDto.getContent());
                note.setCreatedAt(noteDto.getCreatedAt());
                updatedNotes.add(note);
            });

            Set<Long> updatedNoteIds = updatedNotes.stream()
                    .map(Note::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            List<Note> notesToRemove = application.getNotes().stream()
                    .filter(note -> !updatedNoteIds.contains(note.getId()))
                    .toList();

            noteRepository.deleteAll(notesToRemove);

            application.getNotes().clear();
            application.getNotes().addAll(updatedNotes);

            applicationRepository.save(application);
            return true;
        }

        return false;
    }



}
