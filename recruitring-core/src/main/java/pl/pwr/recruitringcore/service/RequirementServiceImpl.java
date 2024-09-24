package pl.pwr.recruitringcore.service;

import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.model.entities.Requirement;
import pl.pwr.recruitringcore.repo.RequirementRepository;

@Service
public class RequirementServiceImpl {

    private final RequirementRepository requirementRepository;

    public RequirementServiceImpl(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    public Requirement findOrCreateByRequirement(String requirementString) {
        return requirementRepository.findByRequirementDescription(requirementString)
                .orElseGet(() -> requirementRepository.save(new Requirement(null, requirementString)));
    }
}