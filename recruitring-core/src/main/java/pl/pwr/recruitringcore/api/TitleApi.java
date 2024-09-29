package pl.pwr.recruitringcore.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.recruitringcore.dto.TitleDTO;

import java.util.List;

@RequestMapping("api/v1/titles")
public interface TitleApi {

    @GetMapping("/search")
    ResponseEntity<List<TitleDTO>> getTitlesByName(@RequestParam String query);

    @PostMapping
    ResponseEntity<TitleDTO> addTitle(@RequestBody String titleName);
}
