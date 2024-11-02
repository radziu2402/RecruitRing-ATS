package pl.pwr.recruitringcore.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.dto.EventDTO;
import pl.pwr.recruitringcore.dto.UserDTO;
import pl.pwr.recruitringcore.model.entities.Event;
import pl.pwr.recruitringcore.model.entities.Recruiter;
import pl.pwr.recruitringcore.repo.EventRepository;
import pl.pwr.recruitringcore.repo.RecruiterRepository;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final RecruiterRepository recruiterRepository;

    public EventServiceImpl(EventRepository eventRepository, RecruiterRepository recruiterRepository) {
        this.eventRepository = eventRepository;
        this.recruiterRepository = recruiterRepository;
    }

    @Override
    public List<EventDTO> getEventsForCurrentUser() {
        UserDTO user = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        Long recruiterId = recruiterRepository.findByUserId(userId)
                .map(Recruiter::getId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found for user ID: " + userId));

        return eventRepository.findByRecruiterId(recruiterId).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public EventDTO createEvent(EventDTO eventDto) {
        UserDTO user = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        Recruiter recruiter = recruiterRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("No recruiter found for the current user"));

        Event event = convertToEntity(eventDto);
        event.setRecruiter(recruiter);

        Event savedEvent = eventRepository.save(event);
        return convertToDto(savedEvent);
    }


    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    private EventDTO convertToDto(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .start(event.getStartTime())
                .end(event.getEndTime())
                .description(event.getDescription())
                .build();
    }

    private Event convertToEntity(EventDTO eventDto) {
        Event event = new Event();
        event.setTitle(eventDto.getTitle());
        event.setStartTime(eventDto.getStart());
        event.setEndTime(eventDto.getEnd());
        event.setDescription(eventDto.getDescription());
        return event;
    }

}
