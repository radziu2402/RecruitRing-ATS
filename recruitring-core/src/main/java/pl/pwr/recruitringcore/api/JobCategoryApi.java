package pl.pwr.recruitringcore.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.recruitringcore.dto.JobCategoryDTO;

import java.util.List;

@RequestMapping("api/v1/job-categories")
public interface JobCategoryApi {

    @GetMapping("/search")
    ResponseEntity<List<JobCategoryDTO>> findJobCategoriesByName(@RequestParam String query);

    @PostMapping
    ResponseEntity<JobCategoryDTO> addJobCategory(@RequestBody String categoryName);
}
