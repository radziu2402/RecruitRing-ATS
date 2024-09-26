package pl.pwr.recruitringcore.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pwr.recruitringcore.dto.LocationDTO;

import java.util.List;

@RequestMapping("api/v1/locations")
public interface LocationApi {

    @GetMapping("/search")
    ResponseEntity<List<LocationDTO>> findLocationsByName(@RequestParam String query);
}
