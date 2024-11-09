package pl.pwr.recruitringcore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pl.pwr.recruitringcore.dto.PublicJobPostingDTO;
import pl.pwr.recruitringcore.dto.PublicJobPostingSummaryDTO;
import pl.pwr.recruitringcore.model.entities.*;
import pl.pwr.recruitringcore.model.enums.WorkType;
import pl.pwr.recruitringcore.repo.JobRepository;

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
class PublicJobServiceImplTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private PublicJobServiceImpl publicJobService;

    @Test
    void shouldReturnAllJobsWithFilters() {
        // GIVEN
        JobPosting jobPosting = createMockJobPosting();
        Page<JobPosting> jobPostingPage = new PageImpl<>(List.of(jobPosting));

        // WHEN
        when(jobRepository.findByFilters(anyLong(), anyLong(), anyLong(), any(WorkType.class), any(PageRequest.class)))
                .thenReturn(jobPostingPage);

        Page<PublicJobPostingSummaryDTO> result = publicJobService.getAllJobs(PageRequest.of(0, 10), 1L, 1L, 1L, WorkType.REMOTE);

        // THEN
        assertEquals(1, result.getTotalElements());
        assertEquals("Software Engineer", result.getContent().getFirst().getTitle());
        verify(jobRepository, times(1)).findByFilters(anyLong(), anyLong(), anyLong(), any(WorkType.class), any(PageRequest.class));
    }

    @Test
    void shouldReturnAllJobsWithoutFilters() {
        // GIVEN
        JobPosting jobPosting = createMockJobPosting();
        Page<JobPosting> jobPostingPage = new PageImpl<>(List.of(jobPosting));

        // WHEN
        when(jobRepository.findAll(any(PageRequest.class))).thenReturn(jobPostingPage);

        Page<PublicJobPostingSummaryDTO> result = publicJobService.getAllJobs(PageRequest.of(0, 10), null, null, null, null);

        // THEN
        assertEquals(1, result.getTotalElements());
        verify(jobRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void shouldReturnJobByOfferCode() {
        // GIVEN
        UUID offerCode = UUID.randomUUID();
        JobPosting jobPosting = createMockJobPosting();
        jobPosting.setOfferCode(offerCode);

        // WHEN
        when(jobRepository.findByOfferCode(offerCode)).thenReturn(Optional.of(jobPosting));

        PublicJobPostingDTO result = publicJobService.getJobByOfferCode(offerCode);

        // THEN
        assertNotNull(result);
        assertEquals("Software Engineer", result.getTitle().getName());
        assertEquals(offerCode, result.getOfferCode());
        verify(jobRepository, times(1)).findByOfferCode(offerCode);
    }

    private JobPosting createMockJobPosting() {
        JobPosting jobPosting = new JobPosting();
        jobPosting.setId(1L);
        jobPosting.setOfferCode(UUID.randomUUID());
        jobPosting.setCreatedAt(LocalDate.now());

        Title title = new Title();
        title.setId(1L);
        title.setName("Software Engineer");
        jobPosting.setTitle(title);

        Location location = new Location();
        location.setId(1L);
        location.setName("New York");
        jobPosting.setLocation(location);

        Requirement requirement = new Requirement();
        requirement.setId(1L);
        requirement.setRequirementDescription("Java knowledge");
        jobPosting.setRequirements(Set.of(requirement));

        JobCategory jobCategory = new JobCategory();
        jobCategory.setId(1L);
        jobCategory.setName("IT");
        jobPosting.setJobCategory(jobCategory);

        jobPosting.setWorkType(WorkType.REMOTE);

        return jobPosting;
    }
}
