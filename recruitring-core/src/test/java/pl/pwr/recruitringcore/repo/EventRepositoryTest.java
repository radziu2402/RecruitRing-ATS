package pl.pwr.recruitringcore.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.pwr.recruitringcore.model.entities.Event;
import pl.pwr.recruitringcore.model.entities.Recruiter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ActiveProfiles("test")
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Test
    void shouldCountUpcomingEventsByRecruiterId() {
        // GIVEN
        Recruiter recruiter = recruiterRepository.save(Recruiter.builder()
                .firstName("Jane")
                .lastName("Doe")
                .position("Recruiter")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build());

        Event upcomingEvent = Event.builder()
                .title("Interview")
                .description("Candidate interview")
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .recruiter(recruiter)
                .build();
        eventRepository.save(upcomingEvent);

        Event pastEvent = Event.builder()
                .title("Past Meeting")
                .description("A past meeting")
                .startTime(LocalDateTime.now().minusDays(1))
                .endTime(LocalDateTime.now().minusDays(1).plusHours(1))
                .recruiter(recruiter)
                .build();
        eventRepository.save(pastEvent);

        // WHEN
        int count = eventRepository.countUpcomingEventsByRecruiterId(recruiter.getId(), LocalDateTime.now());

        // THEN
        assertEquals(1, count);
    }
}
