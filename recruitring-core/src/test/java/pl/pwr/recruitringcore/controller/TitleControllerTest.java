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
import pl.pwr.recruitringcore.dto.TitleDTO;
import pl.pwr.recruitringcore.service.TitleService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TitleControllerTest {

    @Mock
    private TitleService titleService;

    @InjectMocks
    private TitleController titleController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(titleController).build();
    }

    @Test
    void shouldGetTitlesByName() throws Exception {
        String query = "Software Engineer";
        TitleDTO titleDTO = TitleDTO.builder()
                .id(1L)
                .name("Software Engineer")
                .build();
        List<TitleDTO> titles = List.of(titleDTO);

        when(titleService.getTitlesByName(query)).thenReturn(titles);

        mockMvc.perform(get("/api/v1/titles/search")
                        .param("query", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Software Engineer"));

        verify(titleService, times(1)).getTitlesByName(query);
    }

    @Test
    void shouldAddTitle() throws Exception {
        String titleName = "Software Engineer";
        TitleDTO createdTitle = TitleDTO.builder()
                .id(1L)
                .name(titleName)
                .build();

        when(titleService.addNewTitle(any(String.class))).thenReturn(createdTitle);

        mockMvc.perform(post("/api/v1/titles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Software Engineer\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Software Engineer"));

        verify(titleService, times(1)).addNewTitle(any(String.class));
    }
}
