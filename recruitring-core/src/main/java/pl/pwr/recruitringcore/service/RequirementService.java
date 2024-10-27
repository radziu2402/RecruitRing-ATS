package pl.pwr.recruitringcore.service;

import pl.pwr.recruitringcore.dto.RequirementDTO;

import java.util.List;

public interface RequirementService {
    List<RequirementDTO> findRequirementsByDescription(String query);

    RequirementDTO addNewRequirement(String requirementDescription);
}
