package pl.pwr.recruitringcore.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pwr.recruitringcore.dto.RequirementDTO;

import java.util.List;

@RequestMapping("api/v1/requirements")
public interface RequirementApi {

    @GetMapping("/search")
    ResponseEntity<List<RequirementDTO>> findRequirementsByDescription(@RequestParam String query);
}