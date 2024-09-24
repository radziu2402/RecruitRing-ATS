package pl.pwr.recruitringcore.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.recruitringcore.model.entities.JobPosting;
import pl.pwr.recruitringcore.model.enums.WorkType;

public interface JobRepository extends JpaRepository<JobPosting, Long> {
    Page<JobPosting> findByWorkType(WorkType workType, Pageable pageable); // Możemy dodać więcej metod filtrowania
}
