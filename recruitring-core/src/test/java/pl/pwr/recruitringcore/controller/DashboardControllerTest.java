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
import pl.pwr.recruitringcore.dto.DashboardStatsDTO;
import pl.pwr.recruitringcore.service.DashboardService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardController dashboardController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dashboardController).build();
    }

    @Test
    void shouldReturnDashboardStats() throws Exception {
        DashboardStatsDTO statsDTO = DashboardStatsDTO.builder()
                .openRecruitments(10)
                .newCandidates(20)
                .scheduledMeetings(5)
                .avgTimeToHire(30)
                .candidateRatings(4)
                .rejectedCandidates(3)
                .hiredCandidates(7)
                .build();

        when(dashboardService.getDashboardStats()).thenReturn(statsDTO);

        mockMvc.perform(get("/api/v1/dashboard/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openRecruitments").value(10))
                .andExpect(jsonPath("$.newCandidates").value(20))
                .andExpect(jsonPath("$.scheduledMeetings").value(5))
                .andExpect(jsonPath("$.avgTimeToHire").value(30))
                .andExpect(jsonPath("$.candidateRatings").value(4))
                .andExpect(jsonPath("$.rejectedCandidates").value(3))
                .andExpect(jsonPath("$.hiredCandidates").value(7));

        verify(dashboardService, times(1)).getDashboardStats();
    }
}
