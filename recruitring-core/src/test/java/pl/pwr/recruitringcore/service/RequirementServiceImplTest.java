package pl.pwr.recruitringcore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pwr.recruitringcore.dto.RequirementDTO;
import pl.pwr.recruitringcore.model.entities.Requirement;
import pl.pwr.recruitringcore.repo.RequirementRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequirementServiceImplTest {

    @Mock
    private RequirementRepository requirementRepository;

    @InjectMocks
    private RequirementServiceImpl requirementService;

    @Test
    void shouldReturnRequirementsByDescriptionContainingQuery() {
        // GIVEN
        Requirement requirement = new Requirement();
        requirement.setRequirementDescription("Experience with Java");

        // WHEN
        when(requirementRepository.findByRequirementDescriptionContainingIgnoreCase(anyString())).thenReturn(List.of(requirement));
        List<RequirementDTO> result = requirementService.findRequirementsByDescription("Java");

        // THEN
        assertEquals(1, result.size());
        assertEquals("Experience with Java", result.getFirst().getDescription());
        verify(requirementRepository, times(1)).findByRequirementDescriptionContainingIgnoreCase("Java");
    }

    @Test
    void shouldAddNewRequirement() {
        // GIVEN
        Requirement requirement = new Requirement();
        requirement.setId(1L);
        requirement.setRequirementDescription("Knowledge of SQL");

        // WHEN
        when(requirementRepository.save(any(Requirement.class))).thenReturn(requirement);
        RequirementDTO result = requirementService.addNewRequirement("Knowledge of SQL");

        // THEN
        assertNotNull(result);
        assertEquals("Knowledge of SQL", result.getDescription());
        verify(requirementRepository, times(1)).save(any(Requirement.class));
    }
}
