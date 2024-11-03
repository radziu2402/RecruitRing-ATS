package pl.pwr.recruitringcore.service;

import pl.pwr.recruitringcore.dto.EventDTO;

import java.util.List;

public interface EventService {

    List<EventDTO> getEventsForCurrentUser();

    EventDTO createEvent(EventDTO event);

    void deleteEvent(Long eventId);

    void sendMeetingEmail(Long eventId, String candidateEmail);
}
