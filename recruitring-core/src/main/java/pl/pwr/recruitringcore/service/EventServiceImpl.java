package pl.pwr.recruitringcore.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.dto.EventDTO;
import pl.pwr.recruitringcore.dto.UserDTO;
import pl.pwr.recruitringcore.model.entities.Event;
import pl.pwr.recruitringcore.model.entities.Recruiter;
import pl.pwr.recruitringcore.repo.EventRepository;
import pl.pwr.recruitringcore.repo.RecruiterRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final RecruiterRepository recruiterRepository;
    private final EmailService emailService;

    public EventServiceImpl(EventRepository eventRepository, RecruiterRepository recruiterRepository, EmailService emailService) {
        this.eventRepository = eventRepository;
        this.recruiterRepository = recruiterRepository;
        this.emailService = emailService;
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

    @Override
    public void sendMeetingEmail(Long eventId, String candidateEmail) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy 'o godzinie' HH:mm");
        String formattedStartDate = event.getStartTime().format(formatter);
        String formattedEndDate = event.getEndTime().format(formatter);

        String emailContent =
                "<!DOCTYPE html>" +
                        "<html lang=\"pl\">" +
                        "<head>" +
                        "  <meta charset=\"UTF-8\">" +
                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                        "  <style>" +
                        "    body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }" +
                        "    .container { max-width: 600px; margin: 40px auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                        "    .header { text-align: center; padding: 10px 0; background-color: #007bff; color: #fff; border-radius: 8px 8px 0 0; }" +
                        "    .header h1 { margin: 0; font-size: 24px; }" +
                        "    .content { padding: 20px; font-size: 16px; color: #333; }" +
                        "    .content p { margin: 0 0 20px; line-height: 1.6; }" +
                        "    .footer { text-align: center; padding: 10px; font-size: 12px; color: #999; border-top: 1px solid #ddd; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class=\"container\">" +
                        "    <div class=\"header\">" +
                        "      <h1>Potwierdzenie spotkania rekrutacyjnego</h1>" +
                        "    </div>" +
                        "    <div class=\"content\">" +
                        "      <p>Witaj,</p>" +
                        "      <p>Zaplanowaliśmy spotkanie na temat Twojej aplikacji. Poniżej znajdziesz szczegóły:</p>" +
                        "      <p><strong>Temat spotkania: " + event.getTitle() + "</strong></p>" +
                        "      <p><strong>Data i godzina rozpoczęcia:</strong> " + formattedStartDate + "</p>" +
                        "      <p><strong>Data i godzina zakończenia:</strong> " + formattedEndDate + "</p>" +
                        "      <p>Jeśli masz jakiekolwiek pytania, prosimy o kontakt z naszym zespołem rekrutacyjnym.</p>" +
                        "      <p>Z poważaniem,<br>Zespół Rekrutacji</p>" +
                        "    </div>" +
                        "    <div class=\"footer\">" +
                        "      <p>&copy; 2024 RecruitRing. Wszelkie prawa zastrzeżone.</p>" +
                        "    </div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";

        emailService.sendEmail(candidateEmail, "Potwierdzenie spotkania rekrutacyjnego", emailContent);
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
