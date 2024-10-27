package pl.pwr.recruitringcore.service;

import pl.pwr.recruitringcore.dto.LocationDTO;
import java.util.List;

public interface LocationService {
    List<LocationDTO> findLocationsByName(String query);

    LocationDTO addNewLocation(String locationName);
}
