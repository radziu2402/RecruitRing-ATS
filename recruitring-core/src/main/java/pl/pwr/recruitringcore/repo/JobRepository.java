package pl.pwr.recruitringcore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.recruitringcore.model.entities.JobPosting;

import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<JobPosting, Long> {
    Optional<JobPosting> findByOfferCode(UUID offerCode);
}
