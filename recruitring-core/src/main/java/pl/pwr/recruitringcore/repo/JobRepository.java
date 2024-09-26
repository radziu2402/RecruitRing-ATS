package pl.pwr.recruitringcore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.recruitringcore.model.entities.JobPosting;

public interface JobRepository extends JpaRepository<JobPosting, Long> {
}
