package pl.pwr.recruitringcore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pwr.recruitringcore.dto.TitleDTO;
import pl.pwr.recruitringcore.model.entities.Title;
import pl.pwr.recruitringcore.repo.TitleRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TitleServiceImplTest {

    @Mock
    private TitleRepository titleRepository;

    @InjectMocks
    private TitleServiceImpl titleService;

    @Test
    void shouldReturnTitlesByNameContainingQuery() {
        // GIVEN
        Title title = new Title();
        title.setName("Java Developer");

        // WHEN
        when(titleRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(List.of(title));
        List<TitleDTO> result = titleService.getTitlesByName("Java");

        // THEN
        assertEquals(1, result.size());
        assertEquals("Java Developer", result.getFirst().getName());
        verify(titleRepository, times(1)).findByNameContainingIgnoreCase("Java");
    }

    @Test
    void shouldAddNewTitle() {
        // GIVEN
        Title title = new Title();
        title.setId(1L);
        title.setName("New Title");

        // WHEN
        when(titleRepository.save(any(Title.class))).thenReturn(title);
        TitleDTO result = titleService.addNewTitle("New Title");

        // THEN
        assertNotNull(result);
        assertEquals("New Title", result.getName());
        verify(titleRepository, times(1)).save(any(Title.class));
    }
}
