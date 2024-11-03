package pl.pwr.recruitringcore.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.recruitringcore.dto.EventDTO;
import pl.pwr.recruitringcore.dto.MeetingEmailRequestDTO;

import java.util.List;

@RequestMapping("/api/v1/events")
public interface EventApi {

    @GetMapping("/my-events")
    ResponseEntity<List<EventDTO>> getMyEvents();

    @PostMapping
    ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDto);

    @DeleteMapping("/{eventId}")
    ResponseEntity<Void> deleteEvent(@PathVariable Long eventId);

    @PostMapping("/meetings/sendMail")
    ResponseEntity<Void> sendMeetingEmail(@RequestBody MeetingEmailRequestDTO request);
}
