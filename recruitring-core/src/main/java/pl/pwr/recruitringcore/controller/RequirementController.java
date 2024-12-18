package pl.pwr.recruitringcore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.RequirementApi;
import pl.pwr.recruitringcore.dto.RequirementDTO;
import pl.pwr.recruitringcore.service.RequirementService;

import java.util.List;

@RestController
public class RequirementController implements RequirementApi {

    private final RequirementService requirementService;

    public RequirementController(RequirementService requirementService) {
        this.requirementService = requirementService;
    }

    @Override
    public ResponseEntity<List<RequirementDTO>> findRequirementsByDescription(String query) {
        List<RequirementDTO> requirements = requirementService.findRequirementsByDescription(query);
        return ResponseEntity.ok(requirements);
    }

    @Override
    public ResponseEntity<RequirementDTO> addRequirement(String requirementDescription) {
        RequirementDTO createdRequirement = requirementService.addNewRequirement(requirementDescription);
        return ResponseEntity.ok(createdRequirement);
    }
}
