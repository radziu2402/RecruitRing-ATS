package pl.pwr.recruitringcore.service;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pwr.recruitringcore.dto.*;
import pl.pwr.recruitringcore.model.entities.*;
import pl.pwr.recruitringcore.repo.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl {

    private final JobRepository jobRepository;
    private final TitleRepository titleRepository;
    private final LocationRepository locationRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final RequirementRepository requirementRepository;
    private final RecruiterRepository recruiterRepository;

    public JobServiceImpl(JobRepository jobRepository,
                          TitleRepository titleRepository,
                          LocationRepository locationRepository,
                          JobCategoryRepository jobCategoryRepository,
                          RequirementRepository requirementRepository,
                          RecruiterRepository recruiterRepository) {
        this.jobRepository = jobRepository;
        this.titleRepository = titleRepository;
        this.locationRepository = locationRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.requirementRepository = requirementRepository;
        this.recruiterRepository = recruiterRepository;
    }

    public Page<JobPostingSummaryDTO> getAllJobs(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return jobRepository.findAll(pageRequest).map(this::mapToJobPostingSummaryDTO);
    }

    public JobPostingDTO getJobByOfferCode(UUID offerCode) {
        JobPosting jobPosting = jobRepository.findByOfferCode(offerCode)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return mapToJobPostingDTO(jobPosting);
    }

    public void deleteJobPostingByOfferCode(UUID offerCode) {
        JobPosting jobPosting = jobRepository.findByOfferCode(offerCode)
                .orElseThrow(() -> new EntityNotFoundException("Offer not found with code: " + offerCode));
        jobRepository.delete(jobPosting);
    }

    public JobPostingDTO updateJob(UUID offerCode, JobPostingCreationDTO jobCreationDTO) {
        JobPosting existingJobPosting = jobRepository.findByOfferCode(offerCode)
                .orElseThrow(() -> new RuntimeException("Job posting not found"));

        JobPosting updatedJobPosting = mapToJobPostingEntities(jobCreationDTO);

        updatedJobPosting.setId(existingJobPosting.getId());
        updatedJobPosting.setOfferCode(existingJobPosting.getOfferCode());
        updatedJobPosting.setCreatedAt(existingJobPosting.getCreatedAt());

        return mapToJobPostingDTO(jobRepository.save(updatedJobPosting));
    }

    @Transactional
    public JobPostingDTO createJob(JobPostingCreationDTO creationDTO) {
        JobPosting jobPosting = mapToJobPostingEntities(creationDTO);
        jobPosting.setCreatedAt(LocalDate.now());
        return mapToJobPostingDTO(jobRepository.save(jobPosting));
    }


    private JobPosting mapToJobPostingEntities(JobPostingCreationDTO creationDTO) {
        Title title = titleRepository.findById(creationDTO.getTitleId())
                .orElseThrow(() -> new RuntimeException("Invalid title ID"));

        Location location = locationRepository.findById(creationDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Invalid location ID"));

        JobCategory jobCategory = jobCategoryRepository.findById(creationDTO.getJobCategoryId())
                .orElseThrow(() -> new RuntimeException("Invalid job category ID"));

        Set<Requirement> requirements = creationDTO.getRequirementIds().stream()
                .map(id -> requirementRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Invalid requirement ID: " + id)))
                .collect(Collectors.toSet());

        Set<Recruiter> recruiters = creationDTO.getRecruiterIds().stream()
                .map(id -> recruiterRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Invalid recruiter ID: " + id)))
                .collect(Collectors.toSet());

        return JobPosting.builder()
                .title(title)
                .location(location)
                .jobCategory(jobCategory)
                .requirements(requirements)
                .recruiters(recruiters)
                .workType(creationDTO.getWorkType())
                .description(creationDTO.getDescription())
                .build();
    }


    public JobPostingDTO mapToJobPostingDTO(JobPosting jobPosting) {
        return JobPostingDTO.builder()
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
                .recruiters(jobPosting.getRecruiters().stream()
                        .map(this::mapToRecruiterDTO)
                        .toList())
                .jobCategory(mapToJobCategoryDTO(jobPosting.getJobCategory()))
                .build();
    }

    public JobPostingSummaryDTO mapToJobPostingSummaryDTO(JobPosting jobPosting) {
        return JobPostingSummaryDTO.builder()
                .id(jobPosting.getId())
                .title(jobPosting.getTitle().getName())
                .offerCode(jobPosting.getOfferCode())
                .location(jobPosting.getLocation().getName())
                .workType(jobPosting.getWorkType())
                .createdAt(jobPosting.getCreatedAt())
                .recruiters(jobPosting.getRecruiters().stream()
                        .map(recruiter -> recruiter.getFirstName() + " " + recruiter.getLastName() + " - " + recruiter.getPosition())
                        .toList())
                .jobCategory(jobPosting.getJobCategory().getName())
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

    private RecruiterDTO mapToRecruiterDTO(Recruiter recruiter) {
        return RecruiterDTO.builder()
                .id(recruiter.getId())
                .firstName(recruiter.getFirstName())
                .lastName(recruiter.getLastName())
                .position(recruiter.getPosition())
                .email(recruiter.getUser().getEmail())
                .build();
    }

    private JobCategoryDTO mapToJobCategoryDTO(JobCategory jobCategory) {
        return JobCategoryDTO.builder()
                .id(jobCategory.getId())
                .name(jobCategory.getName())
                .build();
    }

}
