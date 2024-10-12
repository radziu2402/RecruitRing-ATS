package pl.pwr.recruitringcore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.recruitringcore.model.entities.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
