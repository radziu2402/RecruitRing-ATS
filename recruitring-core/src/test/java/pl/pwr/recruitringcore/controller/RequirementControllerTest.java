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
import pl.pwr.recruitringcore.dto.RequirementDTO;
import pl.pwr.recruitringcore.service.RequirementService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RequirementControllerTest {

    @Mock
    private RequirementService requirementService;

    @InjectMocks
    private RequirementController requirementController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(requirementController).build();
    }

    @Test
    void shouldFindRequirementsByDescription() throws Exception {
        String query = "Java";
        RequirementDTO requirement1 = RequirementDTO.builder()
                .description("Proficient in Java")
                .build();

        RequirementDTO requirement2 = RequirementDTO.builder()
                .description("Knowledge of Java frameworks")
                .build();

        List<RequirementDTO> requirements = List.of(requirement1, requirement2);

        when(requirementService.findRequirementsByDescription(query)).thenReturn(requirements);

        mockMvc.perform(get("/api/v1/requirements/search")
                        .param("query", query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Proficient in Java"))
                .andExpect(jsonPath("$[1].description").value("Knowledge of Java frameworks"));
    }

    @Test
    void shouldAddRequirement() throws Exception {
        String requirementDescription = "Experience with Spring Boot";
        RequirementDTO createdRequirement = RequirementDTO.builder()
                .description(requirementDescription)
                .build();

        when(requirementService.addNewRequirement(any(String.class))).thenReturn(createdRequirement);

        mockMvc.perform(post("/api/v1/requirements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Experience with Spring Boot\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Experience with Spring Boot"));

        verify(requirementService, times(1)).addNewRequirement(any(String.class));
    }

}
