package pl.pwr.recruitringcore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.RecruiterApi;
import pl.pwr.recruitringcore.dto.RecruiterDTO;
import pl.pwr.recruitringcore.service.RecruiterService;

import java.util.List;

@RestController
public class RecruiterController implements RecruiterApi {

    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @Override
    public ResponseEntity<List<RecruiterDTO>> findRecruitersByName(String query) {
        List<RecruiterDTO> recruiters = recruiterService.findRecruitersByName(query);
        return ResponseEntity.ok(recruiters);
    }
}

