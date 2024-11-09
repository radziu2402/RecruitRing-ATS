package pl.pwr.recruitringcore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pwr.recruitringcore.model.entities.Event;
import pl.pwr.recruitringcore.model.entities.Recruiter;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByRecruiterId(Long recruiterId);

    @Query("SELECT COUNT(e) FROM Event e WHERE e.recruiter.id = :recruiterId AND e.startTime > :currentDateTime")
    int countUpcomingEventsByRecruiterId(Long recruiterId, LocalDateTime currentDateTime);

    void deleteAllByRecruiter(Recruiter recruiter);
}
