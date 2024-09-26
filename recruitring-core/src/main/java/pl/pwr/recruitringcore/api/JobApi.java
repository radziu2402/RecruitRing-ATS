package pl.pwr.recruitringcore.api;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.recruitringcore.dto.JobPostingCreationDTO;
import pl.pwr.recruitringcore.dto.JobPostingDTO;

@RequestMapping("api/v1/jobs")
public interface JobApi {

    @GetMapping
    ResponseEntity<Page<JobPostingDTO>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size);

    @GetMapping("/{id}")
    ResponseEntity<JobPostingDTO> getJobById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<JobPostingDTO> createJob(@RequestBody JobPostingCreationDTO jobCreationDTO);

}
