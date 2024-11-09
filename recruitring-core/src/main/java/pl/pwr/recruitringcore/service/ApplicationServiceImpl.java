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
    private final EmailService emailService;

    public ApplicationServiceImpl(CandidateRepository candidateRepository, ApplicationRepository applicationRepository, NoteRepository noteRepository,
                                  JobRepository jobPostingRepository, AzureBlobStorageService blobStorageService, EmailService emailService) {
        this.candidateRepository = candidateRepository;
        this.applicationRepository = applicationRepository;
        this.noteRepository = noteRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.blobStorageService = blobStorageService;
        this.emailService = emailService;
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
                .rating(0)
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

        Application savedApplication = applicationRepository.save(application);

        Optional<Application> updatedApplication = applicationRepository.findById(savedApplication.getId());
        String applicationCode = String.valueOf(updatedApplication.map(Application::getApplicationCode).orElse(null));

        if (applicationCode == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas generowania kodu aplikacji.");
        }

        sendApplicationConfirmationEmail(candidate, applicationCode, optionalJobPosting.get().getTitle().getName());

        return ResponseEntity.ok().build();
    }

    public void sendApplicationConfirmationEmail(Candidate candidate, String offerCode, String jobTitle) {
        String emailContent =
                "<!DOCTYPE html>" +
                        "<html lang=\"pl\">" +
                        "<head>" +
                        "  <meta charset=\"UTF-8\">" +
                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                        "  <style>" +
                        "    body { font-family: Arial, sans-serif; background-color: #f4f4f4; }" +
                        "    .container { max-width: 600px; margin: 40px auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                        "    .header { text-align: center; padding: 10px 0; background-color: #007bff; color: #fff; border-radius: 8px 8px 0 0; }" +
                        "    .header h1 { font-size: 24px; }" +
                        "    .content { padding: 20px; font-size: 16px; color: #333; }" +
                        "    .footer { text-align: center; padding: 10px; font-size: 12px; color: #999; border-top: 1px solid #ddd; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class=\"container\">" +
                        "    <div class=\"header\">" +
                        "      <h1>Potwierdzenie aplikacji</h1>" +
                        "    </div>" +
                        "    <div class=\"content\">" +
                        "      <p>Witaj " + candidate.getFirstName() + ",</p>" +
                        "      <p>Dziękujemy za złożenie aplikacji na stanowisko: <strong>" + jobTitle + "</strong>.</p>" +
                        "      <p>Twój kod aplikacji: <strong>" + offerCode + "</strong></p>" +
                        "      <p>Możesz śledzić status aplikacji, wpisując ten kod na naszej stronie.</p>" +
                        "      <p>Z poważaniem,<br>Zespół Rekrutacji</p>" +
                        "    </div>" +
                        "    <div class=\"footer\">" +
                        "      <p>&copy; 2024 RecruitRing. Wszelkie prawa zastrzeżone.</p>" +
                        "    </div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";

        emailService.sendEmail(candidate.getEmail(), "Potwierdzenie aplikacji", emailContent);
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
            ApplicationStatus newStatus = ApplicationStatus.valueOf(candidateDto.getStatus());

            if (newStatus == ApplicationStatus.HIRED && application.getHiredAt() == null) {
                application.setHiredAt(LocalDateTime.now());
            } else if (newStatus != ApplicationStatus.HIRED) {
                application.setHiredAt(null);
            }

            application.setRating(candidateDto.getRating());
            application.setStatus(newStatus);

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

    @Override
    public ApplicationStatusDTO getApplicationStatus(String applicationCode) {
        if (!isValidUUID(applicationCode)) {
            throw new IllegalArgumentException("Nieprawidłowy format kodu aplikacji: " + applicationCode);
        }

        Optional<Application> applicationOpt = applicationRepository.findByApplicationCode(UUID.fromString(applicationCode));

        if (applicationOpt.isPresent()) {
            Application application = applicationOpt.get();
            return ApplicationStatusDTO.builder()
                    .applicationCode(application.getApplicationCode().toString())
                    .status(application.getStatus().toString())
                    .positionName(application.getJobPosting().getTitle().getName())
                    .candidateFirstName(application.getCandidate().getFirstName())
                    .candidateLastName(application.getCandidate().getLastName())
                    .applicationDate(application.getAppliedAt())
                    .build();
        } else {
            throw new NoSuchElementException("Nie znaleziono aplikacji o podanym kodzie: " + applicationCode);
        }
    }

    private boolean isValidUUID(String code) {
        try {
            UUID.fromString(code);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
