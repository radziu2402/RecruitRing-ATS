package pl.pwr.recruitringcore.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.recruitringcore.dto.LocationDTO;

import java.util.List;

@RequestMapping("api/v1/locations")
public interface LocationApi {

    @GetMapping("/search")
    ResponseEntity<List<LocationDTO>> findLocationsByName(@RequestParam String query);

    @PostMapping
    ResponseEntity<LocationDTO> addLocation(@RequestBody String locationName);
}
