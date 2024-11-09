package pl.pwr.recruitringcore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.pwr.recruitringcore.dto.JobPostingCreationDTO;
import pl.pwr.recruitringcore.dto.JobPostingDTO;
import pl.pwr.recruitringcore.dto.RecruiterJobPostingDTO;
import pl.pwr.recruitringcore.dto.TitleDTO;
import pl.pwr.recruitringcore.service.JobService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class JobControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    private UUID testOfferCode;
    private JobPostingDTO jobPostingDTO;
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();
        testOfferCode = UUID.randomUUID();

        jobPostingDTO = new JobPostingDTO();
        jobPostingDTO.setOfferCode(testOfferCode);
        jobPostingDTO.setTitle(TitleDTO.builder().name("Software Engineer").build());
        jobPostingDTO.setDescription("Job Description");
    }

    @Test
    void shouldGetJobByOfferCode() throws Exception {
        when(jobService.getJobByOfferCode(testOfferCode)).thenReturn(jobPostingDTO);

        mockMvc.perform(get("/api/v1/jobs/" + testOfferCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.offerCode").value(testOfferCode.toString()))
                .andExpect(jsonPath("$.title.name").value("Software Engineer"));
    }

    @Test
    void shouldCreateJob() throws Exception {
        when(jobService.createJob(any(JobPostingCreationDTO.class))).thenReturn(jobPostingDTO);

        mockMvc.perform(post("/api/v1/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":{\"name\":\"Software Engineer\"},\"description\":\"Job Description\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title.name").value("Software Engineer"));
    }

    @Test
    void shouldUpdateJob() throws Exception {
        jobPostingDTO.setTitle(TitleDTO.builder().name("Updated Title").build());
        when(jobService.updateJob(any(UUID.class), any(JobPostingCreationDTO.class))).thenReturn(jobPostingDTO);

        mockMvc.perform(put("/api/v1/jobs/" + testOfferCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":{\"name\":\"Updated Title\"},\"description\":\"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title.name").value("Updated Title"));
    }

    @Test
    void shouldDeleteJobPosting() throws Exception {
        mockMvc.perform(delete("/api/v1/jobs/" + testOfferCode))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetRecruiterJobPostings() throws Exception {
        RecruiterJobPostingDTO recruiterJobPostingDTO = new RecruiterJobPostingDTO();
        recruiterJobPostingDTO.setTitle("Recruiter Job Title");

        List<RecruiterJobPostingDTO> recruiterJobPostings = List.of(recruiterJobPostingDTO);
        when(jobService.getRecruiterJobPostings()).thenReturn(recruiterJobPostings);

        mockMvc.perform(get("/api/v1/jobs/recruiter/assigned"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Recruiter Job Title"));
    }
}
