package pl.pwr.recruitringcore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pwr.recruitringcore.dto.LocationDTO;
import pl.pwr.recruitringcore.model.entities.Location;
import pl.pwr.recruitringcore.repo.LocationRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationServiceImpl locationService;

    @Test
    void shouldReturnLocationsByNameContainingQuery() {
        // GIVEN
        Location location = new Location();
        location.setName("New York");

        // WHEN
        when(locationRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(List.of(location));

        List<LocationDTO> result = locationService.findLocationsByName("New");

        // THEN
        assertEquals(1, result.size());
        assertEquals("New York", result.getFirst().getName());
        verify(locationRepository, times(1)).findByNameContainingIgnoreCase("New");
    }

    @Test
    void shouldAddNewLocation() {
        // GIVEN
        Location location = new Location();
        location.setId(1L);
        location.setName("Los Angeles");

        // WHEN
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        LocationDTO result = locationService.addNewLocation("Los Angeles");

        // THEN
        assertNotNull(result);
        assertEquals("Los Angeles", result.getName());
        verify(locationRepository, times(1)).save(any(Location.class));
    }
}
