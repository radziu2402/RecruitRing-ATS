package pl.pwr.recruitringcore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.recruitringcore.model.entities.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
}