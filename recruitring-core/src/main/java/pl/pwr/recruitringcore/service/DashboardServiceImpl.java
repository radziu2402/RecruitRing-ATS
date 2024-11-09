package pl.pwr.recruitringcore.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.dto.DashboardStatsDTO;
import pl.pwr.recruitringcore.dto.UserDTO;
import pl.pwr.recruitringcore.model.entities.Recruiter;
import pl.pwr.recruitringcore.repo.ApplicationRepository;
import pl.pwr.recruitringcore.repo.EventRepository;
import pl.pwr.recruitringcore.repo.JobRepository;
import pl.pwr.recruitringcore.repo.RecruiterRepository;

import java.time.LocalDateTime;


@Service
public class DashboardServiceImpl implements DashboardService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;
    private final EventRepository eventRepository;

    public DashboardServiceImpl(ApplicationRepository applicationRepository, JobRepository jobRepository, RecruiterRepository recruiterRepository, EventRepository eventRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public DashboardStatsDTO getDashboardStats() {
        Long userId = ((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Long recruiterId = recruiterRepository.findByUserId(userId)
                .map(Recruiter::getId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found for user ID: " + userId));

        int openRecruitments = jobRepository.countOpenRecruitmentsByRecruiterId(recruiterId);
        int newCandidates = applicationRepository.countNewCandidatesByRecruiterId(recruiterId);
        LocalDateTime now = LocalDateTime.now();
        int scheduledMeetings = eventRepository.countUpcomingEventsByRecruiterId(recruiterId, now);
        int avgTimeToHire = applicationRepository.calculateAvgTimeToHireByRecruiterId(recruiterId);
        int candidateRatings = applicationRepository.calculateCandidateRatingsPercentageByRecruiterId(recruiterId);
        int rejectedCandidates = applicationRepository.calculateRejectedCandidatesPercentageByRecruiterId(recruiterId);
        int hiredCandidates = applicationRepository.calculateHiredCandidatesPercentageByRecruiterId(recruiterId);

        return new DashboardStatsDTO(
                openRecruitments,
                newCandidates,
                scheduledMeetings,
                avgTimeToHire,
                candidateRatings,
                rejectedCandidates,
                hiredCandidates
        );
    }
}
