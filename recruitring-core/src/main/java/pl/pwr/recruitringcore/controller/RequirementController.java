package pl.pwr.recruitringcore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.RequirementApi;
import pl.pwr.recruitringcore.dto.RequirementDTO;
import pl.pwr.recruitringcore.service.RequirementServiceImpl;

import java.util.List;

@RestController
public class RequirementController implements RequirementApi {

    private final RequirementServiceImpl requirementService;

    public RequirementController(RequirementServiceImpl requirementService) {
        this.requirementService = requirementService;
    }

    @Override
    public ResponseEntity<List<RequirementDTO>> findRequirementsByDescription(String query) {
        List<RequirementDTO> requirements = requirementService.findRequirementsByDescription(query);
        return ResponseEntity.ok(requirements);
    }
}
