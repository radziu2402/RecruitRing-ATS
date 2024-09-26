package pl.pwr.recruitringcore.service;

import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.dto.RequirementDTO;
import pl.pwr.recruitringcore.model.entities.Requirement;
import pl.pwr.recruitringcore.repo.RequirementRepository;

import java.util.List;

@Service
public class RequirementServiceImpl {

    private final RequirementRepository requirementRepository;

    public RequirementServiceImpl(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    public List<RequirementDTO> findRequirementsByDescription(String query) {
        return requirementRepository.findByRequirementDescriptionContainingIgnoreCase(query)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private RequirementDTO mapToDTO(Requirement requirement) {
        RequirementDTO dto = new RequirementDTO();
        dto.setId(requirement.getId());
        dto.setDescription(requirement.getRequirementDescription());
        return dto;
    }
}