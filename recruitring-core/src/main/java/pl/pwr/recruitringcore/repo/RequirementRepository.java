package pl.pwr.recruitringcore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.recruitringcore.model.entities.Requirement;

import java.util.Optional;

public interface RequirementRepository extends JpaRepository<Requirement, Integer> {

    Optional<Requirement> findByRequirementDescription(String requirementDescription);

}
