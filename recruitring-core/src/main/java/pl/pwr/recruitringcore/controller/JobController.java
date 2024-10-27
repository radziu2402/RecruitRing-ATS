package pl.pwr.recruitringcore.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.JobApi;
import pl.pwr.recruitringcore.dto.JobPostingCreationDTO;
import pl.pwr.recruitringcore.dto.JobPostingDTO;
import pl.pwr.recruitringcore.dto.JobPostingSummaryDTO;
import pl.pwr.recruitringcore.dto.RecruiterJobPostingDTO;
import pl.pwr.recruitringcore.service.JobServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
public class JobController implements JobApi {

    private final JobServiceImpl jobServiceImpl;

    public JobController(JobServiceImpl jobServiceImpl) {
        this.jobServiceImpl = jobServiceImpl;
    }

    @Override
    public ResponseEntity<Page<JobPostingSummaryDTO>> getAllJobs(int page, int size) {
        Page<JobPostingSummaryDTO> jobs = jobServiceImpl.getAllJobs(page, size);
        return ResponseEntity.ok(jobs);
    }

    @Override
    public ResponseEntity<JobPostingDTO> getJobByOfferCode(UUID offerCode) {
        JobPostingDTO job = jobServiceImpl.getJobByOfferCode(offerCode);
        return ResponseEntity.ok(job);
    }

    @Override
    public ResponseEntity<JobPostingDTO> updateJob(UUID offerCode, JobPostingCreationDTO jobCreationDTO) {
        return ResponseEntity.ok(jobServiceImpl.updateJob(offerCode, jobCreationDTO));
    }

    @Override
    public ResponseEntity<JobPostingDTO> createJob(JobPostingCreationDTO jobCreationDTO) {
        JobPostingDTO createdJob = jobServiceImpl.createJob(jobCreationDTO);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteJobPosting(UUID offerCode) {
        jobServiceImpl.deleteJobPostingByOfferCode(offerCode);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<RecruiterJobPostingDTO>> getRecruiterJobPostings() {
        List<RecruiterJobPostingDTO> jobPostings = jobServiceImpl.getRecruiterJobPostings();
        return ResponseEntity.ok(jobPostings);
    }

}
