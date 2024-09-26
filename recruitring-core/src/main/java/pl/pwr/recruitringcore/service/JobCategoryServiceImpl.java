package pl.pwr.recruitringcore.service;

import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.dto.JobCategoryDTO;
import pl.pwr.recruitringcore.model.entities.JobCategory;
import pl.pwr.recruitringcore.repo.JobCategoryRepository;

import java.util.List;

@Service
public class JobCategoryServiceImpl {

    private final JobCategoryRepository jobCategoryRepository;

    public JobCategoryServiceImpl(JobCategoryRepository jobCategoryRepository) {
        this.jobCategoryRepository = jobCategoryRepository;
    }

    public List<JobCategoryDTO> findJobCategoriesByName(String query) {
        return jobCategoryRepository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private JobCategoryDTO mapToDTO(JobCategory jobCategory) {
        JobCategoryDTO dto = new JobCategoryDTO();
        dto.setId(jobCategory.getId());
        dto.setName(jobCategory.getName());
        return dto;
    }
}
