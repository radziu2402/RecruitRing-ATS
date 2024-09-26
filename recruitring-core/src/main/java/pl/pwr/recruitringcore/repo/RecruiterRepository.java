package pl.pwr.recruitringcore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.recruitringcore.model.entities.Recruiter;

import java.util.List;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    List<Recruiter> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}