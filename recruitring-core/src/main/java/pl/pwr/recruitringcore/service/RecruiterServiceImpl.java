package pl.pwr.recruitringcore.service;

import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.model.entities.Recruiter;
import pl.pwr.recruitringcore.repo.RecruiterRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    private final RecruiterRepository recruiterRepository;

    public RecruiterServiceImpl(RecruiterRepository recruiterRepository) {
        this.recruiterRepository = recruiterRepository;
    }

    @Override
    public Optional<Recruiter> findByFirstNameAndLastName(String firstName, String lastName) {
        return recruiterRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public Recruiter save(Recruiter recruiter) {
        return recruiterRepository.save(recruiter);
    }

    @Override
    public Set<Recruiter> findOrCreateRecruiters(Set<String> recruiterNames) {
        return recruiterNames.stream().map(name -> {
            String[] split = name.split(" ");
            String firstName = split[0];
            String lastName = split[1];

            // Szukaj rekrutera na podstawie imienia i nazwiska lub go utwórz
            return recruiterRepository.findByFirstNameAndLastName(firstName, lastName)
                    .orElseGet(() -> save(Recruiter.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .build()));
        }).collect(Collectors.toSet());
    }
}
