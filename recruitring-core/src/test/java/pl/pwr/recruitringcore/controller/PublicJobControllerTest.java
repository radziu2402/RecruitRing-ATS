package pl.pwr.recruitringcore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.pwr.recruitringcore.dto.LocationDTO;
import pl.pwr.recruitringcore.dto.PublicJobPostingDTO;
import pl.pwr.recruitringcore.dto.TitleDTO;
import pl.pwr.recruitringcore.model.enums.WorkType;
import pl.pwr.recruitringcore.service.PublicJobService;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PublicJobControllerTest {

    @Mock
    private PublicJobService publicJobService;

    @InjectMocks
    private PublicJobController publicJobController;

    private MockMvc mockMvc;
    private UUID testOfferCode;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(publicJobController).build();
        testOfferCode = UUID.randomUUID();
    }

    @Test
    void shouldGetAllJobs() throws Exception {
        mockMvc.perform(get("/api/v1/public-jobs")
                        .param("page", "0")
                        .param("size", "10")
                        .param("titleId", "1")
                        .param("locationId", "1")
                        .param("jobCategoryId", "1")
                        .param("workType", "HYBRID"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetJobByOfferCode() throws Exception {
        PublicJobPostingDTO publicJobPostingDTO = new PublicJobPostingDTO();
        publicJobPostingDTO.setOfferCode(testOfferCode);
        publicJobPostingDTO.setTitle(TitleDTO.builder().name("Software Engineer").build());
        publicJobPostingDTO.setLocation(LocationDTO.builder().name("New York").build());
        publicJobPostingDTO.setWorkType(WorkType.HYBRID);

        when(publicJobService.getJobByOfferCode(testOfferCode)).thenReturn(publicJobPostingDTO);

        mockMvc.perform(get("/api/v1/public-jobs/" + testOfferCode))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.offerCode").value(testOfferCode.toString()))
                .andExpect(jsonPath("$.title.name").value("Software Engineer"))
                .andExpect(jsonPath("$.location.name").value("New York"))
                .andExpect(jsonPath("$.workType").value("HYBRID"));
    }
}
