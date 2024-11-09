package pl.pwr.recruitringcore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pwr.recruitringcore.dto.JobCategoryDTO;
import pl.pwr.recruitringcore.model.entities.JobCategory;
import pl.pwr.recruitringcore.repo.JobCategoryRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobCategoryServiceImplTest {

    @Mock
    private JobCategoryRepository jobCategoryRepository;

    @InjectMocks
    private JobCategoryServiceImpl jobCategoryService;

    @Test
    void shouldReturnJobCategoriesByNameContainingQuery() {
        // GIVEN
        JobCategory jobCategory = new JobCategory();
        jobCategory.setName("Engineering");

        // WHEN
        when(jobCategoryRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(List.of(jobCategory));
        List<JobCategoryDTO> result = jobCategoryService.findJobCategoriesByName("Eng");

        // THEN
        assertEquals(1, result.size());
        assertEquals("Engineering", result.stream().findFirst().orElseThrow().getName());
        verify(jobCategoryRepository, times(1)).findByNameContainingIgnoreCase("Eng");
    }

    @Test
    void shouldAddNewJobCategory() {
        // GIVEN
        JobCategory jobCategory = new JobCategory();
        jobCategory.setId(1L);
        jobCategory.setName("Finance");

        // WHEN
        when(jobCategoryRepository.save(any(JobCategory.class))).thenReturn(jobCategory);
        JobCategoryDTO result = jobCategoryService.addNewJobCategory("Finance");

        // THEN
        assertNotNull(result);
        assertEquals("Finance", result.getName());
        verify(jobCategoryRepository, times(1)).save(any(JobCategory.class));
    }
}
