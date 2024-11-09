package pl.pwr.recruitringcore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.pwr.recruitringcore.blobstorage.AzureBlobStorageService;
import pl.pwr.recruitringcore.dto.ApplicationDTO;
import pl.pwr.recruitringcore.dto.ApplicationStatusDTO;
import pl.pwr.recruitringcore.model.entities.Application;
import pl.pwr.recruitringcore.model.entities.Candidate;
import pl.pwr.recruitringcore.model.entities.JobPosting;
import pl.pwr.recruitringcore.model.entities.Title;
import pl.pwr.recruitringcore.model.enums.ApplicationStatus;
import pl.pwr.recruitringcore.repo.ApplicationRepository;
import pl.pwr.recruitringcore.repo.CandidateRepository;
import pl.pwr.recruitringcore.repo.JobRepository;
import pl.pwr.recruitringcore.repo.NoteRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private JobRepository jobPostingRepository;

    @Mock
    private AzureBlobStorageService blobStorageService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Mock
    private MultipartFile cvFile;

    @Test
    void shouldProcessApplicationSuccessfully() throws IOException {
        // GIVEN
        UUID offerCode = UUID.randomUUID();
        ApplicationDTO applicationDto = new ApplicationDTO();
        applicationDto.setEmail("test@example.com");
        applicationDto.setFirstName("John");
        applicationDto.setLastName("Doe");
        applicationDto.setPhone("123456789");
        applicationDto.setOfferCode(offerCode);

        Candidate candidate = Candidate.builder()
                .email(applicationDto.getEmail())
                .build();

        JobPosting jobPosting = JobPosting.builder().title(Title.builder().name("title").build()).offerCode(offerCode).build();

        when(candidateRepository.findByEmail(applicationDto.getEmail())).thenReturn(Optional.of(candidate));
        when(jobPostingRepository.findByOfferCode(offerCode)).thenReturn(Optional.of(jobPosting));
        when(blobStorageService.upload(cvFile, applicationDto.getEmail() + "_CV.pdf"))
                .thenReturn(ResponseEntity.ok("cvFileName.pdf"));
        UUID applicationCode = UUID.randomUUID();
        Application savedApplication = Application.builder()
                .applicationCode(applicationCode)
                .status(ApplicationStatus.NEW)
                .jobPosting(JobPosting.builder().title(Title.builder().name("title").build()).build())
                .candidate(Candidate.builder().firstName("John").lastName("Doe").build())
                .appliedAt(LocalDateTime.now())
                .build();
        when(applicationRepository.save(any(Application.class))).thenReturn(savedApplication);

        // WHEN
        ResponseEntity<String> response = applicationService.processApplication(applicationDto, cvFile);

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(applicationRepository, times(1)).save(any(Application.class));
        verify(emailService, times(1)).sendEmail(eq(applicationDto.getEmail()), eq("Potwierdzenie aplikacji"), anyString());
    }

    @Test
    void shouldReturnBadRequestWhenJobNotFound() {
        // GIVEN
        UUID offerCode = UUID.randomUUID();
        ApplicationDTO applicationDto = new ApplicationDTO();
        applicationDto.setEmail("test@example.com");
        applicationDto.setOfferCode(offerCode);

        when(jobPostingRepository.findByOfferCode(offerCode)).thenReturn(Optional.empty());

        // WHEN
        ResponseEntity<String> response = applicationService.processApplication(applicationDto, cvFile);

        // THEN
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Oferta pracy nie została znaleziona.", response.getBody());
    }

    @Test
    void shouldReturnInternalServerErrorWhenFileUploadFails() throws IOException {
        // GIVEN
        UUID offerCode = UUID.randomUUID();
        ApplicationDTO applicationDto = new ApplicationDTO();
        applicationDto.setEmail("test@example.com");
        applicationDto.setOfferCode(offerCode);

        JobPosting jobPosting = JobPosting.builder()
                .offerCode(offerCode)
                .build();

        when(jobPostingRepository.findByOfferCode(offerCode)).thenReturn(Optional.of(jobPosting));
        when(blobStorageService.upload(cvFile, applicationDto.getEmail() + "_CV.pdf"))
                .thenThrow(new IOException("File upload error"));

        // WHEN
        ResponseEntity<String> response = applicationService.processApplication(applicationDto, cvFile);

        // THEN
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Błąd podczas przesyłania pliku"));
    }

    @Test
    void shouldGetApplicationStatusSuccessfully() {
        // GIVEN
        UUID applicationCode = UUID.randomUUID();
        Application application = Application.builder()
                .applicationCode(applicationCode)
                .status(ApplicationStatus.NEW)
                .jobPosting(JobPosting.builder().title(Title.builder().name("title").build()).build())
                .candidate(Candidate.builder().firstName("John").lastName("Doe").build())
                .appliedAt(LocalDateTime.now())
                .build();

        when(applicationRepository.findByApplicationCode(applicationCode)).thenReturn(Optional.of(application));

        // WHEN
        ApplicationStatusDTO statusDTO = applicationService.getApplicationStatus(applicationCode.toString());

        // THEN
        assertEquals(applicationCode.toString(), statusDTO.getApplicationCode());
        assertEquals(ApplicationStatus.NEW.toString(), statusDTO.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenApplicationCodeIsInvalid() {
        // GIVEN
        String invalidApplicationCode = "invalidCode";

        // WHEN THEN
        assertThrows(IllegalArgumentException.class, () -> applicationService.getApplicationStatus(invalidApplicationCode));
    }

    @Test
    void shouldReturnNotFoundWhenApplicationDoesNotExist() {
        // GIVEN
        UUID applicationCode = UUID.randomUUID();
        when(applicationRepository.findByApplicationCode(applicationCode)).thenReturn(Optional.empty());

        // WHEN THEN
        String codeString = applicationCode.toString();
        assertThrows(NoSuchElementException.class, () -> applicationService.getApplicationStatus(codeString));
    }

}
