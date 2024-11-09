package pl.pwr.recruitringcore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.pwr.recruitringcore.dto.*;
import pl.pwr.recruitringcore.model.entities.*;
import pl.pwr.recruitringcore.model.enums.ApplicationStatus;
import pl.pwr.recruitringcore.repo.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceImplTest {

    @Mock
    private JobRepository jobRepository;
    @Mock
    private TitleRepository titleRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private JobCategoryRepository jobCategoryRepository;
    @Mock
    private RequirementRepository requirementRepository;
    @Mock
    private RecruiterRepository recruiterRepository;
    @InjectMocks
    private JobServiceImpl jobService;

    private JobPosting jobPosting;
    private Title title;
    private Location location;
    private JobCategory jobCategory;
    private Requirement requirement;
    private Recruiter recruiter;
    private JobPostingCreationDTO jobCreationDTO;

    @BeforeEach
    void setUpMocks() {
        // GIVEN
        jobPosting = new JobPosting();
        jobPosting.setId(1L);
        jobPosting.setCreatedAt(LocalDate.now());
        jobPosting.setOfferCode(UUID.randomUUID());

        title = new Title();
        title.setId(1L);
        title.setName("Test Title");
        jobPosting.setTitle(title);

        location = new Location();
        location.setId(1L);
        location.setName("Test Location");
        jobPosting.setLocation(location);

        jobCategory = new JobCategory();
        jobCategory.setId(1L);
        jobCategory.setName("Test Category");
        jobPosting.setJobCategory(jobCategory);

        requirement = new Requirement();
        requirement.setId(1L);
        requirement.setRequirementDescription("Test Requirement");
        jobPosting.setRequirements(Set.of(requirement));

        recruiter = new Recruiter();
        User user = new User();
        user.setEmail("test@example.com");
        recruiter.setUser(user);
        recruiter.setId(1L);
        jobPosting.setRecruiters(Set.of(recruiter));

        Application application = new Application();
        application.setId(1L);
        application.setStatus(ApplicationStatus.NEW);
        jobPosting.setApplications(Set.of(application));

        jobCreationDTO = new JobPostingCreationDTO();
        jobCreationDTO.setTitleId(1L);
        jobCreationDTO.setLocationId(1L);
        jobCreationDTO.setJobCategoryId(1L);
        jobCreationDTO.setRequirementIds(List.of(1L));
        jobCreationDTO.setRecruiterIds(List.of(1L));
    }

    @Test
    void shouldReturnAllJobsWithPagination() {
        // GIVEN
        Page<JobPosting> jobPostingPage = new PageImpl<>(List.of(jobPosting));

        // WHEN
        when(jobRepository.findAll(any(PageRequest.class))).thenReturn(jobPostingPage);

        Page<JobPostingSummaryDTO> result = jobService.getAllJobs(0, 10);

        // THEN
        assertEquals(1, result.getTotalElements());
        verify(jobRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void shouldReturnJobByOfferCode() {
        // GIVEN
        UUID offerCode = jobPosting.getOfferCode();

        // WHEN
        when(jobRepository.findByOfferCode(offerCode)).thenReturn(Optional.of(jobPosting));

        JobPostingDTO result = jobService.getJobByOfferCode(offerCode);

        // THEN
        assertNotNull(result);
        assertEquals(offerCode, result.getOfferCode());
        verify(jobRepository, times(1)).findByOfferCode(offerCode);
    }

    @Test
    void shouldDeleteJobPostingByOfferCode() {
        // GIVEN
        UUID offerCode = jobPosting.getOfferCode();

        // WHEN
        when(jobRepository.findByOfferCode(offerCode)).thenReturn(Optional.of(jobPosting));

        jobService.deleteJobPostingByOfferCode(offerCode);

        // THEN
        verify(jobRepository, times(1)).delete(jobPosting);
    }

    @Test
    void shouldUpdateJobByOfferCode() {
        // GIVEN
        UUID offerCode = jobPosting.getOfferCode();

        // WHEN
        when(jobRepository.findByOfferCode(offerCode)).thenReturn(Optional.of(jobPosting));
        when(jobRepository.save(any(JobPosting.class))).thenReturn(jobPosting);
        when(titleRepository.findById(1L)).thenReturn(Optional.of(title));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(jobCategoryRepository.findById(1L)).thenReturn(Optional.of(jobCategory));
        when(requirementRepository.findById(1L)).thenReturn(Optional.of(requirement));
        when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter));

        JobPostingDTO result = jobService.updateJob(offerCode, jobCreationDTO);

        // THEN
        assertNotNull(result);
        assertEquals(offerCode, result.getOfferCode());
        verify(jobRepository, times(1)).save(any(JobPosting.class));
    }

    @Test
    void shouldCreateJobPosting() {
        // GIVEN
        // WHEN
        when(jobRepository.save(any(JobPosting.class))).thenReturn(jobPosting);
        when(titleRepository.findById(1L)).thenReturn(Optional.of(title));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(jobCategoryRepository.findById(1L)).thenReturn(Optional.of(jobCategory));
        when(requirementRepository.findById(1L)).thenReturn(Optional.of(requirement));
        when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter));

        JobPostingDTO result = jobService.createJob(jobCreationDTO);

        // THEN
        assertNotNull(result);
        verify(jobRepository, times(1)).save(any(JobPosting.class));
    }

    @Test
    void shouldReturnRecruiterJobPostings() {
        // GIVEN
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");

        // WHEN
        when(authentication.getPrincipal()).thenReturn(userDTO);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(recruiterRepository.findByUserEmail(anyString())).thenReturn(Optional.of(recruiter));
        when(jobRepository.findByRecruiterId(anyLong())).thenReturn(List.of(jobPosting));

        List<RecruiterJobPostingDTO> result = jobService.getRecruiterJobPostings();

        // THEN
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
        verify(jobRepository, times(1)).findByRecruiterId(anyLong());
    }

}
