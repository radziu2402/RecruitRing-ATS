package pl.pwr.recruitringcore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.pwr.recruitringcore.dto.EventDTO;
import pl.pwr.recruitringcore.dto.UserDTO;
import pl.pwr.recruitringcore.model.entities.Event;
import pl.pwr.recruitringcore.model.entities.Recruiter;
import pl.pwr.recruitringcore.repo.EventRepository;
import pl.pwr.recruitringcore.repo.RecruiterRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EventServiceImpl eventService;

    private Recruiter recruiter;
    private Event event;
    private EventDTO eventDto;

    @BeforeEach
    void setup() {
        // GIVEN
        recruiter = new Recruiter();
        recruiter.setId(1L);

        event = new Event();
        event.setId(1L);
        event.setTitle("Interview Meeting");
        event.setStartTime(LocalDateTime.now());
        event.setEndTime(LocalDateTime.now().plusHours(1));

        eventDto = EventDTO.builder()
                .title("Interview Meeting")
                .start(event.getStartTime())
                .end(event.getEndTime())
                .build();
    }

    @Test
    void shouldReturnEventsForCurrentUser() {
        // GIVEN
        UserDTO user = new UserDTO();
        user.setId(1L);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(recruiterRepository.findByUserId(1L)).thenReturn(Optional.of(recruiter));
        when(eventRepository.findByRecruiterId(1L)).thenReturn(List.of(event));

        // WHEN
        List<EventDTO> events = eventService.getEventsForCurrentUser();

        // THEN
        assertEquals(1, events.size());
        assertEquals("Interview Meeting", events.getFirst().getTitle());
        verify(eventRepository, times(1)).findByRecruiterId(1L);
    }

    @Test
    void shouldCreateEvent() {
        // GIVEN
        UserDTO user = new UserDTO();
        user.setId(1L);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(recruiterRepository.findByUserId(1L)).thenReturn(Optional.of(recruiter));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // WHEN
        EventDTO result = eventService.createEvent(eventDto);

        // THEN
        assertNotNull(result);
        assertEquals("Interview Meeting", result.getTitle());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void shouldDeleteEvent() {
        // GIVEN
        Long eventId = 1L;

        // WHEN
        eventService.deleteEvent(eventId);

        // THEN
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    void shouldSendMeetingEmail() {
        // GIVEN
        Long eventId = 1L;
        String candidateEmail = "candidate@example.com";

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // WHEN
        eventService.sendMeetingEmail(eventId, candidateEmail);

        // THEN
        verify(emailService, times(1)).sendEmail(eq(candidateEmail), anyString(), anyString());
    }
}
