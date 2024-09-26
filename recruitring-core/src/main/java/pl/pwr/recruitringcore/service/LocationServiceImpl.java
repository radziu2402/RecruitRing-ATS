package pl.pwr.recruitringcore.service;

import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.dto.LocationDTO;
import pl.pwr.recruitringcore.model.entities.Location;
import pl.pwr.recruitringcore.repo.LocationRepository;

import java.util.List;

@Service
public class LocationServiceImpl {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationDTO> findLocationsByName(String query) {
        List<Location> locations = locationRepository.findByNameContainingIgnoreCase(query);
        return locations.stream().map(this::mapToDTO).toList();
    }

    private LocationDTO mapToDTO(Location location) {
        return LocationDTO.builder()
                .id(location.getId())
                .name(location.getName())
                .build();
    }
}
