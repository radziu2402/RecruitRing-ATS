package pl.pwr.recruitringcore.service;

import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.dto.LocationDTO;
import pl.pwr.recruitringcore.model.entities.Location;
import pl.pwr.recruitringcore.repo.LocationRepository;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<LocationDTO> findLocationsByName(String query) {
        List<Location> locations = locationRepository.findByNameContainingIgnoreCase(query);
        return locations.stream().map(this::mapToDTO).toList();
    }

    @Override
    public LocationDTO addNewLocation(String locationName) {
        Location location = new Location();
        location.setName(locationName);

        Location savedLocation = locationRepository.save(location);

        return mapToDTO(savedLocation);
    }

    private LocationDTO mapToDTO(Location location) {
        return LocationDTO.builder()
                .id(location.getId())
                .name(location.getName())
                .build();
    }
}
