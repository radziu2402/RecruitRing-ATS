package pl.pwr.recruitringcore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.LocationApi;
import pl.pwr.recruitringcore.dto.LocationDTO;
import pl.pwr.recruitringcore.service.LocationServiceImpl;

import java.util.List;

@RestController
public class LocationController implements LocationApi {

    private final LocationServiceImpl locationService;

    public LocationController(LocationServiceImpl locationService) {
        this.locationService = locationService;
    }

    @Override
    public ResponseEntity<List<LocationDTO>> findLocationsByName(String query) {
        List<LocationDTO> locations = locationService.findLocationsByName(query);
        return ResponseEntity.ok(locations);
    }

    @Override
    public ResponseEntity<LocationDTO> addLocation(String locationName) {
        LocationDTO createdLocation = locationService.addNewLocation(locationName);
        return ResponseEntity.ok(createdLocation);
    }
}

