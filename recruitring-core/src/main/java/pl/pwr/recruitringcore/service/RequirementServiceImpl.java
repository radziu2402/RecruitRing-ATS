package pl.pwr.recruitringcore.service;

import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.dto.RequirementDTO;
import pl.pwr.recruitringcore.model.entities.Requirement;
import pl.pwr.recruitringcore.repo.RequirementRepository;

import java.util.List;

@Service
public class RequirementServiceImpl implements RequirementService {

    private final RequirementRepository requirementRepository;

    public RequirementServiceImpl(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    @Override
    public List<RequirementDTO> findRequirementsByDescription(String query) {
        return requirementRepository.findByRequirementDescriptionContainingIgnoreCase(query)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public RequirementDTO addNewRequirement(String requirementDescription) {
        Requirement requirement = new Requirement();
        requirement.setRequirementDescription(requirementDescription);

        Requirement savedRequirement = requirementRepository.save(requirement);

        return mapToDTO(savedRequirement);
    }

    private RequirementDTO mapToDTO(Requirement requirement) {
        RequirementDTO dto = new RequirementDTO();
        dto.setId(requirement.getId());
        dto.setDescription(requirement.getRequirementDescription());
        return dto;
    }
}
