package pl.pwr.recruitringcore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.recruitringcore.model.entities.Title;

import java.util.List;

public interface TitleRepository extends JpaRepository<Title, Long> {
    List<Title> findByNameContainingIgnoreCase(String name);
}
