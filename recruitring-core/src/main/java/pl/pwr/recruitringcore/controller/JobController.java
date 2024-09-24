package pl.pwr.recruitringcore.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.JobApi;
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
    public ResponseEntity<JobPostingDTO> createJob(JobPostingDTO jobDTO) {
        JobPostingDTO newJob = jobServiceImpl.createJob(jobDTO);
        return ResponseEntity.status(201).body(newJob);
    }

    @Override
    public ResponseEntity<JobPostingDTO> updateJob(Long id, JobPostingDTO jobDTO) {
        JobPostingDTO updatedJob = jobServiceImpl.updateJob(id, jobDTO);
        return ResponseEntity.ok(updatedJob);
    }

    @Override
    public ResponseEntity<Void> deleteJob(Long id) {
        jobServiceImpl.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

}
