package pl.pwr.recruitringcore.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pwr.recruitringcore.dto.JobPostingCreationDTO;
import pl.pwr.recruitringcore.dto.JobPostingDTO;
import pl.pwr.recruitringcore.model.entities.*;
import pl.pwr.recruitringcore.repo.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
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

    public Page<JobPostingDTO> getAllJobs(int page, int size) {
        return jobRepository.findAll(PageRequest.of(page, size)).map(this::mapToDTO);
    }

    public JobPostingDTO getJobById(Long id) {
        JobPosting jobPosting = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return mapToDTO(jobPosting);
    }

    @Transactional
    public JobPostingDTO createJob(JobPostingCreationDTO creationDTO) {
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

        JobPosting jobPosting = JobPosting.builder()
                .title(title)
                .location(location)
                .jobCategory(jobCategory)
                .requirements(requirements)
                .recruiters(recruiters)
                .workType(creationDTO.getWorkType())
                .description(creationDTO.getDescription())
                .createdAt(LocalDate.now())
                .build();

        return mapToDTO(jobRepository.save(jobPosting));
    }


    private JobPostingDTO mapToDTO(JobPosting jobPosting) {
        List<String> recruiterNames = jobPosting.getRecruiters().stream()
                .map(recruiter -> recruiter.getFirstName() + " " + recruiter.getLastName())
                .toList();

        return JobPostingDTO.builder()
                .id(jobPosting.getId())
                .title(jobPosting.getTitle().getName())
                .description(jobPosting.getDescription())
                .requirements(jobPosting.getRequirements().stream()
                        .map(Requirement::getRequirementDescription)
                        .toList())
                .location(jobPosting.getLocation().getName())
                .workType(jobPosting.getWorkType())
                .createdAt(jobPosting.getCreatedAt())
                .recruiters(recruiterNames)
                .jobCategory(jobPosting.getJobCategory().getName())
                .build();
    }

}
