package pl.pwr.recruitringcore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.recruitringcore.model.entities.JobCategory;

import java.util.List;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    List<JobCategory> findByNameContainingIgnoreCase(String query);
}