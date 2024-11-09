package pl.pwr.recruitringcore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.pwr.recruitringcore.dto.LocationDTO;
import pl.pwr.recruitringcore.service.LocationServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    @Mock
    private LocationServiceImpl locationService;

    @InjectMocks
    private LocationController locationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
    }

    @Test
    void shouldFindLocationsByName() throws Exception {
        LocationDTO location = new LocationDTO();
        location.setName("New York");

        when(locationService.findLocationsByName("New")).thenReturn(List.of(location));

        mockMvc.perform(get("/api/v1/locations/search")
                        .param("query", "New"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("New York"));
    }

    @Test
    void shouldAddLocation() throws Exception {
        String locationName = "Los Angeles";
        LocationDTO createdLocation = new LocationDTO();
        createdLocation.setName(locationName);

        when(locationService.addNewLocation(any(String.class))).thenReturn(createdLocation);

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"" + locationName + "\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(locationName));

        verify(locationService, times(1)).addNewLocation(any(String.class));
    }

}
