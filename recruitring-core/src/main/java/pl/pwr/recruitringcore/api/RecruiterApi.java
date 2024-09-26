package pl.pwr.recruitringcore.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pwr.recruitringcore.dto.RecruiterDTO;

import java.util.List;

@RequestMapping("api/v1/recruiters")
public interface RecruiterApi {

    @GetMapping("/search")
    ResponseEntity<List<RecruiterDTO>> findRecruitersByName(@RequestParam String query);
}