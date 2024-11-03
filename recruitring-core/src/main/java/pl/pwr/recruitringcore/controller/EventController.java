package pl.pwr.recruitringcore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.EventApi;
import pl.pwr.recruitringcore.dto.EventDTO;
import pl.pwr.recruitringcore.dto.MeetingEmailRequestDTO;
import pl.pwr.recruitringcore.service.EventService;

import java.util.List;

@RestController
public class EventController implements EventApi {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public ResponseEntity<List<EventDTO>> getMyEvents() {
        List<EventDTO> events = eventService.getEventsForCurrentUser();
        return ResponseEntity.ok(events);
    }

    @Override
    public ResponseEntity<EventDTO> createEvent(EventDTO eventDto) {
        return ResponseEntity.ok(eventService.createEvent(eventDto));
    }

    @Override
    public ResponseEntity<Void> deleteEvent(Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> sendMeetingEmail(MeetingEmailRequestDTO meetingEmailRequestDTO) {
        eventService.sendMeetingEmail(meetingEmailRequestDTO.getEventId(), meetingEmailRequestDTO.getCandidateEmail());
        return ResponseEntity.ok().build();
    }
}
