package pl.pwr.recruitringcore.api;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.recruitringcore.dto.JobPostingCreationDTO;
import pl.pwr.recruitringcore.dto.JobPostingDTO;
import pl.pwr.recruitringcore.dto.JobPostingSummaryDTO;
import pl.pwr.recruitringcore.dto.RecruiterJobPostingDTO;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/jobs")
public interface JobApi {

    @GetMapping
    ResponseEntity<Page<JobPostingSummaryDTO>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size);

    @GetMapping("/{offerCode}")
    ResponseEntity<JobPostingDTO> getJobByOfferCode(@PathVariable UUID offerCode);

    @PostMapping
    ResponseEntity<JobPostingDTO> createJob(@RequestBody JobPostingCreationDTO jobCreationDTO);

    @PutMapping("/{offerCode}")
    ResponseEntity<JobPostingDTO> updateJob(
            @PathVariable UUID offerCode,
            @RequestBody JobPostingCreationDTO jobCreationDTO);

    @DeleteMapping("/{offerCode}")
    ResponseEntity<Void> deleteJobPosting(@PathVariable UUID offerCode);

    @GetMapping("/recruiter/assigned")
    ResponseEntity<List<RecruiterJobPostingDTO>> getRecruiterJobPostings();

}
