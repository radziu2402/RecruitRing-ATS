package pl.pwr.recruitringcore.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.recruitringcore.dto.RequirementDTO;

import java.util.List;

@RequestMapping("api/v1/requirements")
public interface RequirementApi {

    @GetMapping("/search")
    ResponseEntity<List<RequirementDTO>> findRequirementsByDescription(@RequestParam String query);

    @PostMapping
    ResponseEntity<RequirementDTO> addRequirement(@RequestBody String requirementDescription);
}