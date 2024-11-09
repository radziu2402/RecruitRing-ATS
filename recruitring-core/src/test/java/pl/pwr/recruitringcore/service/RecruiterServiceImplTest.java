package pl.pwr.recruitringcore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pwr.recruitringcore.dto.RecruiterDTO;
import pl.pwr.recruitringcore.model.entities.Recruiter;
import pl.pwr.recruitringcore.model.entities.User;
import pl.pwr.recruitringcore.repo.RecruiterRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecruiterServiceImplTest {

    @Mock
    private RecruiterRepository recruiterRepository;

    @InjectMocks
    private RecruiterServiceImpl recruiterService;

    @Test
    void shouldReturnRecruitersByNameContainingQuery() {
        // GIVEN
        Recruiter recruiter = new Recruiter();
        recruiter.setFirstName("John");
        recruiter.setLastName("Doe");
        recruiter.setPosition("HR Manager");
        User user = new User();
        user.setEmail("john.doe@example.com");
        recruiter.setUser(user);

        // WHEN
        when(recruiterRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(anyString(), anyString()))
                .thenReturn(List.of(recruiter));

        List<RecruiterDTO> result = recruiterService.findRecruitersByName("John");

        // THEN
        assertEquals(1, result.size());
        assertEquals("John", result.getFirst().getFirstName());
        assertEquals("Doe", result.getFirst().getLastName());
        assertEquals("HR Manager", result.getFirst().getPosition());
        assertEquals("john.doe@example.com", result.getFirst().getEmail());
        verify(recruiterRepository, times(1)).findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("John", "John");
    }
}
