package pl.pwr.recruitringcore.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.JobApi;
import pl.pwr.recruitringcore.dto.JobPostingCreationDTO;
import pl.pwr.recruitringcore.dto.JobPostingDTO;
import pl.pwr.recruitringcore.service.JobServiceImpl;

@RestController
public class JobController implements JobApi {

    private final JobServiceImpl jobServiceImpl;

    public JobController(JobServiceImpl jobServiceImpl) {
        this.jobServiceImpl = jobServiceImpl;
    }

    @Override
    public ResponseEntity<Page<JobPostingDTO>> getAllJobs(int page, int size) {
        Page<JobPostingDTO> jobs = jobServiceImpl.getAllJobs(page, size);
        return ResponseEntity.ok(jobs);
    }

    @Override
    public ResponseEntity<JobPostingDTO> getJobById(Long id) {
        JobPostingDTO job = jobServiceImpl.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @Override
    public ResponseEntity<JobPostingDTO> createJob(JobPostingCreationDTO jobCreationDTO) {
        JobPostingDTO createdJob = jobServiceImpl.createJob(jobCreationDTO);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

}
