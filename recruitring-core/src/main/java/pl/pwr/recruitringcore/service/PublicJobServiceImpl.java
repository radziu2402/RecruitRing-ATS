package pl.pwr.recruitringcore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.dto.*;
import pl.pwr.recruitringcore.model.entities.*;
import pl.pwr.recruitringcore.model.enums.WorkType;
import pl.pwr.recruitringcore.repo.JobRepository;

import java.util.UUID;

@Service
public class PublicJobServiceImpl {

    private final JobRepository jobRepository;

    public PublicJobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Page<PublicJobPostingSummaryDTO> getAllJobs(PageRequest pageRequest, Long titleId, Long locationId, Long jobCategoryId, WorkType workType) {
        Page<JobPosting> jobPostings;

        if (titleId != null || locationId != null || jobCategoryId != null || workType != null) {
            jobPostings = jobRepository.findByFilters(titleId, locationId, jobCategoryId, workType, pageRequest);
        } else {
            jobPostings = jobRepository.findAll(pageRequest);
        }

        return jobPostings.map(this::maptoPublicJobPostingSummaryDTO);
    }

    public PublicJobPostingDTO getJobByOfferCode(UUID offerCode) {
        JobPosting jobPosting = jobRepository.findByOfferCode(offerCode)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return maptoPublicJobPostingDTO(jobPosting);
    }

    private PublicJobPostingSummaryDTO maptoPublicJobPostingSummaryDTO(JobPosting jobPosting) {
        return PublicJobPostingSummaryDTO.builder()
                .id(jobPosting.getId())
                .title(jobPosting.getTitle().getName())
                .offerCode(jobPosting.getOfferCode())
                .location(jobPosting.getLocation().getName())
                .workType(jobPosting.getWorkType())
                .createdAt(jobPosting.getCreatedAt())
                .jobCategory(jobPosting.getJobCategory().getName())
                .build();
    }

    private PublicJobPostingDTO maptoPublicJobPostingDTO(JobPosting jobPosting) {
        return PublicJobPostingDTO.builder()
                .id(jobPosting.getId())
                .title(mapToTitleDTO(jobPosting.getTitle()))
                .description(jobPosting.getDescription())
                .offerCode(jobPosting.getOfferCode())
                .requirements(jobPosting.getRequirements().stream()
                        .map(this::mapToRequirementDTO)
                        .toList())
                .location(mapToLocationDTO(jobPosting.getLocation()))
                .workType(jobPosting.getWorkType())
                .createdAt(jobPosting.getCreatedAt())
                .jobCategory(mapToJobCategoryDTO(jobPosting.getJobCategory()))
                .build();
    }

    private TitleDTO mapToTitleDTO(Title title) {
        return TitleDTO.builder()
                .id(title.getId())
                .name(title.getName())
                .build();
    }

    private RequirementDTO mapToRequirementDTO(Requirement requirement) {
        return RequirementDTO.builder()
                .id(requirement.getId())
                .description(requirement.getRequirementDescription())
                .build();
    }

    private LocationDTO mapToLocationDTO(Location location) {
        return LocationDTO.builder()
                .id(location.getId())
                .name(location.getName())
                .build();
    }

    private JobCategoryDTO mapToJobCategoryDTO(JobCategory jobCategory) {
        return JobCategoryDTO.builder()
                .id(jobCategory.getId())
                .name(jobCategory.getName())
                .build();
    }
}
