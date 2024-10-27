package pl.pwr.recruitringcore.service;

import org.springframework.data.domain.Page;
import pl.pwr.recruitringcore.dto.JobPostingCreationDTO;
import pl.pwr.recruitringcore.dto.JobPostingDTO;
import pl.pwr.recruitringcore.dto.JobPostingSummaryDTO;
import pl.pwr.recruitringcore.dto.RecruiterJobPostingDTO;

import java.util.List;
import java.util.UUID;

public interface JobService {
    Page<JobPostingSummaryDTO> getAllJobs(int page, int size);

    JobPostingDTO getJobByOfferCode(UUID offerCode);

    void deleteJobPostingByOfferCode(UUID offerCode);

    JobPostingDTO updateJob(UUID offerCode, JobPostingCreationDTO jobCreationDTO);

    JobPostingDTO createJob(JobPostingCreationDTO creationDTO);

    List<RecruiterJobPostingDTO> getRecruiterJobPostings();
}
