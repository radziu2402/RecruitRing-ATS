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
import pl.pwr.recruitringcore.dto.EventDTO;
import pl.pwr.recruitringcore.dto.MeetingEmailRequestDTO;
import pl.pwr.recruitringcore.service.EventService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    void shouldReturnMyEvents() throws Exception {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(1L);
        eventDTO.setTitle("Event Title");

        when(eventService.getEventsForCurrentUser()).thenReturn(List.of(eventDTO));

        mockMvc.perform(get("/api/v1/events/my-events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Event Title"));

        verify(eventService, times(1)).getEventsForCurrentUser();
    }

    @Test
    void shouldCreateEvent() throws Exception {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(2L);
        eventDTO.setTitle("New Event");

        when(eventService.createEvent(any(EventDTO.class))).thenReturn(eventDTO);

        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"New Event\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.title").value("New Event"));

        verify(eventService, times(1)).createEvent(any(EventDTO.class));
    }

    @Test
    void shouldDeleteEvent() throws Exception {
        Long eventId = 3L;

        mockMvc.perform(delete("/api/v1/events/{eventId}", eventId))
                .andExpect(status().isNoContent());

        verify(eventService, times(1)).deleteEvent(eventId);
    }

    @Test
    void shouldSendMeetingEmail() throws Exception {
        MeetingEmailRequestDTO meetingRequest = new MeetingEmailRequestDTO();
        meetingRequest.setEventId(4L);
        meetingRequest.setCandidateEmail("candidate@example.com");

        mockMvc.perform(post("/api/v1/events/meetings/sendMail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"eventId\": 4, \"candidateEmail\": \"candidate@example.com\"}"))
                .andExpect(status().isOk());

        verify(eventService, times(1)).sendMeetingEmail(4L, "candidate@example.com");
    }
}
