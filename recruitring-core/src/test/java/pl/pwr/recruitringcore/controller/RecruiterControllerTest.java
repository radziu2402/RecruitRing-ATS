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
import pl.pwr.recruitringcore.dto.RecruiterDTO;
import pl.pwr.recruitringcore.service.RecruiterService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RecruiterControllerTest {

    @Mock
    private RecruiterService recruiterService;

    @InjectMocks
    private RecruiterController recruiterController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recruiterController).build();
    }

    @Test
    void shouldFindRecruitersByName() throws Exception {
        String query = "John";
        RecruiterDTO recruiter1 = RecruiterDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .position("Recruiter")
                .email("john@example.com")
                .build();

        RecruiterDTO recruiter2 = RecruiterDTO.builder()
                .firstName("John")
                .lastName("Smith")
                .position("Senior Recruiter")
                .email("john.smith@example.com")
                .build();

        List<RecruiterDTO> recruiters = List.of(recruiter1, recruiter2);

        when(recruiterService.findRecruitersByName(query)).thenReturn(recruiters);

        mockMvc.perform(get("/api/v1/recruiters/search")
                        .param("query", query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].firstName").value("John"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));
    }
}
