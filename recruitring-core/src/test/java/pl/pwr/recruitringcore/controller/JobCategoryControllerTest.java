package pl.pwr.recruitringcore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.pwr.recruitringcore.dto.JobCategoryDTO;
import pl.pwr.recruitringcore.service.JobCategoryService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class JobCategoryControllerTest {

    @Mock
    private JobCategoryService jobCategoryService;

    @InjectMocks
    private JobCategoryController jobCategoryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(jobCategoryController).build();
    }

    @Test
    void shouldFindJobCategoriesByName() throws Exception {
        String query = "Developer";
        JobCategoryDTO categoryDTO = new JobCategoryDTO();
        categoryDTO.setName("Software Developer");

        when(jobCategoryService.findJobCategoriesByName(query)).thenReturn(List.of(categoryDTO));

        mockMvc.perform(get("/api/v1/job-categories/search")
                        .param("query", query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Software Developer"));

        verify(jobCategoryService, times(1)).findJobCategoriesByName(query);
    }

    @Test
    void shouldAddJobCategory() throws Exception {
        String categoryName = "Data Scientist";
        JobCategoryDTO createdCategory = new JobCategoryDTO();
        createdCategory.setName(categoryName);

        when(jobCategoryService.addNewJobCategory(any(String.class))).thenReturn(createdCategory);

        mockMvc.perform(post("/api/v1/job-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Data Scientist\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Data Scientist"));

        verify(jobCategoryService, times(1)).addNewJobCategory(any(String.class));
    }

}
