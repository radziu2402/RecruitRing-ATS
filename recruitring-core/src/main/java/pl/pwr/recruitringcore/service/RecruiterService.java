package pl.pwr.recruitringcore.service;

import pl.pwr.recruitringcore.model.entities.Recruiter;

import java.util.Optional;
import java.util.Set;

public interface RecruiterService {

    Optional<Recruiter> findByFirstNameAndLastName(String firstName, String lastName);

    Recruiter save(Recruiter recruiter);

    Set<Recruiter> findOrCreateRecruiters(Set<String> recruiterNames);
}
