package pl.pwr.recruitringcore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.pwr.recruitringcore.blobstorage.AzureBlobStorageService;
import pl.pwr.recruitringcore.dto.ApplicationDTO;
import pl.pwr.recruitringcore.dto.ApplicationStatusDTO;
import pl.pwr.recruitringcore.dto.CandidateDTO;
import pl.pwr.recruitringcore.dto.DetailedCandidateDTO;
import pl.pwr.recruitringcore.service.ApplicationService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ApplicationControllerTest {

    @Mock
    private ApplicationService applicationService;

    @Mock
    private AzureBlobStorageService azureBlobStorageService;

    @InjectMocks
    private ApplicationController applicationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(applicationController).build();
    }

    @Test
    void shouldCreateApplication() throws Exception {
        MockMultipartFile applicationFile = new MockMultipartFile(
                "application",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                "{\"login\":\"newUser\",\"email\":\"newUser@example.com\",\"isLocked\":false}".getBytes()
        );
        MockMultipartFile cvFile = new MockMultipartFile("cv", "cv.pdf", MediaType.APPLICATION_PDF_VALUE, "cv content".getBytes());

        when(applicationService.processApplication(any(ApplicationDTO.class), any())).thenReturn(ResponseEntity.ok("Application created"));

        mockMvc.perform(multipart("/api/v1/apply")
                        .file(applicationFile)
                        .file(cvFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("Application created"));

        verify(applicationService, times(1)).processApplication(any(ApplicationDTO.class), any());
    }


    @Test
    void shouldGetCandidatesByOfferCode() throws Exception {
        String offerCode = "exampleOfferCode";
        List<CandidateDTO> candidates = Collections.singletonList(new CandidateDTO());
        when(applicationService.getCandidatesByOfferCode(offerCode)).thenReturn(candidates);

        mockMvc.perform(get("/api/v1/recruitments/{offerCode}/candidates", offerCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(applicationService, times(1)).getCandidatesByOfferCode(offerCode);
    }

    @Test
    void shouldGetCandidateDetails() throws Exception {
        String applicationCode = UUID.randomUUID().toString();
        DetailedCandidateDTO candidateDetails = new DetailedCandidateDTO();
        when(applicationService.getCandidateDetails(UUID.fromString(applicationCode))).thenReturn(candidateDetails);

        mockMvc.perform(get("/api/v1/recruitments/candidates/{applicationCode}", applicationCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(applicationService, times(1)).getCandidateDetails(UUID.fromString(applicationCode));
    }

    @Test
    void shouldGetFileUrlToDownload() throws Exception {
        String blobName = "fileName.pdf";
        String fileUrl = "https://example.com/fileName.pdf";
        when(azureBlobStorageService.getFileUrlToDownload(blobName)).thenReturn(ResponseEntity.ok(fileUrl));

        mockMvc.perform(get("/api/v1/documents/download-url/{blobName}", blobName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(fileUrl));

        verify(azureBlobStorageService, times(1)).getFileUrlToDownload(blobName);
    }

    @Test
    void shouldUpdateCandidate() throws Exception {
        String applicationCode = UUID.randomUUID().toString();
        DetailedCandidateDTO candidateDto = new DetailedCandidateDTO();
        candidateDto.setFirstName("John");
        candidateDto.setLastName("Doe");

        when(applicationService.updateCandidate(eq(UUID.fromString(applicationCode)), any(DetailedCandidateDTO.class)))
                .thenReturn(true);

        mockMvc.perform(put("/api/v1/recruitments/candidates/{applicationCode}", applicationCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\", \"lastName\":\"Doe\"}"))
                .andExpect(status().isOk());

        verify(applicationService, times(1)).updateCandidate(eq(UUID.fromString(applicationCode)), any(DetailedCandidateDTO.class));
    }


    @Test
    void shouldGetApplicationStatus() throws Exception {
        String applicationCode = UUID.randomUUID().toString();
        ApplicationStatusDTO applicationStatus = new ApplicationStatusDTO();
        when(applicationService.getApplicationStatus(applicationCode)).thenReturn(applicationStatus);

        mockMvc.perform(get("/api/v1/applications/status/{applicationCode}", applicationCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(applicationService, times(1)).getApplicationStatus(applicationCode);
    }
}
