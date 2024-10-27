package pl.pwr.recruitringcore.service;

import pl.pwr.recruitringcore.dto.JobCategoryDTO;

import java.util.List;

public interface JobCategoryService {
    List<JobCategoryDTO> findJobCategoriesByName(String query);

    JobCategoryDTO addNewJobCategory(String categoryName);
}
