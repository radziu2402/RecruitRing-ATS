package pl.pwr.recruitringcore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.pwr.recruitringcore.dto.DashboardStatsDTO;
import pl.pwr.recruitringcore.dto.UserDTO;
import pl.pwr.recruitringcore.model.entities.Recruiter;
import pl.pwr.recruitringcore.repo.ApplicationRepository;
import pl.pwr.recruitringcore.repo.EventRepository;
import pl.pwr.recruitringcore.repo.JobRepository;
import pl.pwr.recruitringcore.repo.RecruiterRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    void shouldReturnDashboardStats() {
        // GIVEN
        Long userId = 1L;
        Long recruiterId = 1L;
        int openRecruitments = 5;
        int newCandidates = 10;
        int scheduledMeetings = 3;
        int avgTimeToHire = 15;
        int candidateRatings = 80;
        int rejectedCandidates = 20;
        int hiredCandidates = 30;

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDTO, null));

        Recruiter recruiter = new Recruiter();
        recruiter.setId(recruiterId);

        when(recruiterRepository.findByUserId(userId)).thenReturn(Optional.of(recruiter));
        when(jobRepository.countOpenRecruitmentsByRecruiterId(recruiterId)).thenReturn(openRecruitments);
        when(applicationRepository.countNewCandidatesByRecruiterId(recruiterId)).thenReturn(newCandidates);
        when(eventRepository.countUpcomingEventsByRecruiterId(eq(recruiterId), any(LocalDateTime.class))).thenReturn(scheduledMeetings);
        when(applicationRepository.calculateAvgTimeToHireByRecruiterId(recruiterId)).thenReturn(avgTimeToHire);
        when(applicationRepository.calculateCandidateRatingsPercentageByRecruiterId(recruiterId)).thenReturn(candidateRatings);
        when(applicationRepository.calculateRejectedCandidatesPercentageByRecruiterId(recruiterId)).thenReturn(rejectedCandidates);
        when(applicationRepository.calculateHiredCandidatesPercentageByRecruiterId(recruiterId)).thenReturn(hiredCandidates);

        // WHEN
        DashboardStatsDTO result = dashboardService.getDashboardStats();

        // THEN
        assertEquals(openRecruitments, result.getOpenRecruitments());
        assertEquals(newCandidates, result.getNewCandidates());
        assertEquals(scheduledMeetings, result.getScheduledMeetings());
        assertEquals(avgTimeToHire, result.getAvgTimeToHire());
        assertEquals(candidateRatings, result.getCandidateRatings());
        assertEquals(rejectedCandidates, result.getRejectedCandidates());
        assertEquals(hiredCandidates, result.getHiredCandidates());

        verify(recruiterRepository, times(1)).findByUserId(userId);
        verify(jobRepository, times(1)).countOpenRecruitmentsByRecruiterId(recruiterId);
        verify(applicationRepository, times(1)).countNewCandidatesByRecruiterId(recruiterId);
        verify(eventRepository, times(1)).countUpcomingEventsByRecruiterId(eq(recruiterId), any(LocalDateTime.class));
        verify(applicationRepository, times(1)).calculateAvgTimeToHireByRecruiterId(recruiterId);
        verify(applicationRepository, times(1)).calculateCandidateRatingsPercentageByRecruiterId(recruiterId);
        verify(applicationRepository, times(1)).calculateRejectedCandidatesPercentageByRecruiterId(recruiterId);
        verify(applicationRepository, times(1)).calculateHiredCandidatesPercentageByRecruiterId(recruiterId);
    }
}
