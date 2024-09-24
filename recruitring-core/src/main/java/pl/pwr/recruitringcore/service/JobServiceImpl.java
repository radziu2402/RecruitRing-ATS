package pl.pwr.recruitringcore.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pwr.recruitringcore.dto.JobPostingDTO;
import pl.pwr.recruitringcore.model.entities.JobPosting;
import pl.pwr.recruitringcore.model.entities.Recruiter;
import pl.pwr.recruitringcore.model.entities.Requirement;
import pl.pwr.recruitringcore.repo.JobRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl {

    private final JobRepository jobRepository;
    private final RequirementServiceImpl requirementService;
    private final RecruiterServiceImpl recruiterService;

    public JobServiceImpl(JobRepository jobRepository, RequirementServiceImpl requirementServiceImpl, RecruiterServiceImpl recruiterService) {
        this.jobRepository = jobRepository;
        this.requirementService = requirementServiceImpl;
        this.recruiterService = recruiterService;
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
    public JobPostingDTO createJob(JobPostingDTO jobDTO) {
        JobPosting jobPosting = mapToEntity(jobDTO);
        jobPosting.setCreatedAt(LocalDate.now());
        return mapToDTO(jobRepository.save(jobPosting));
    }

    @Transactional
    public JobPostingDTO updateJob(Long id, JobPostingDTO jobDTO) {
        JobPosting jobPosting = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        jobPosting.setTitle(jobDTO.getTitle());
        jobPosting.setDescription(jobDTO.getDescription());
        jobPosting.setLocation(jobDTO.getLocation());

        Set<Recruiter> recruiters = recruiterService.findOrCreateRecruiters(Set.copyOf(jobDTO.getRecruiters()));
        jobPosting.setRecruiters(recruiters);

        return mapToDTO(jobRepository.save(jobPosting));
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    private JobPostingDTO mapToDTO(JobPosting jobPosting) {
        List<String> recruiterNames = jobPosting.getRecruiters().stream()
                .map(recruiter -> recruiter.getFirstName() + " " + recruiter.getLastName())
                .toList();

        return JobPostingDTO.builder()
                .id(jobPosting.getId())
                .title(jobPosting.getTitle())
                .description(jobPosting.getDescription())
                .requirements(jobPosting.getRequirements().stream()
                        .map(Requirement::getRequirementDescription)
                        .toList())
                .location(jobPosting.getLocation())
                .workType(jobPosting.getWorkType())
                .createdAt(jobPosting.getCreatedAt())
                .recruiters(recruiterNames)
                .jobCategory(jobPosting.getJobCategory())
                .build();
    }

    public JobPosting mapToEntity(JobPostingDTO jobPostingDTO) {
        Set<Requirement> requirements = jobPostingDTO.getRequirements().stream()
                .map(requirementService::findOrCreateByRequirement)
                .collect(Collectors.toSet());

        Set<Recruiter> recruiters = recruiterService.findOrCreateRecruiters(
                new HashSet<>(jobPostingDTO.getRecruiters())
        );

        return JobPosting.builder()
                .id(jobPostingDTO.getId())
                .title(jobPostingDTO.getTitle())
                .description(jobPostingDTO.getDescription())
                .requirements(requirements)
                .location(jobPostingDTO.getLocation())
                .workType(jobPostingDTO.getWorkType())
                .createdAt(jobPostingDTO.getCreatedAt())
                .recruiters(recruiters)
                .jobCategory(jobPostingDTO.getJobCategory())
                .build();
    }


}
