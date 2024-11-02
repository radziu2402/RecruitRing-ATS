package pl.pwr.recruitringcore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.JobCategoryApi;
import pl.pwr.recruitringcore.dto.JobCategoryDTO;
import pl.pwr.recruitringcore.service.JobCategoryService;

import java.util.List;

@RestController
public class JobCategoryController implements JobCategoryApi {

    private final JobCategoryService jobCategoryService;

    public JobCategoryController(JobCategoryService jobCategoryService) {
        this.jobCategoryService = jobCategoryService;
    }

    @Override
    public ResponseEntity<List<JobCategoryDTO>> findJobCategoriesByName(String query) {
        List<JobCategoryDTO> jobCategories = jobCategoryService.findJobCategoriesByName(query);
        return ResponseEntity.ok(jobCategories);
    }

    @Override
    public ResponseEntity<JobCategoryDTO> addJobCategory(String categoryName) {
        JobCategoryDTO createdCategory = jobCategoryService.addNewJobCategory(categoryName);
        return ResponseEntity.ok(createdCategory);
    }
}
