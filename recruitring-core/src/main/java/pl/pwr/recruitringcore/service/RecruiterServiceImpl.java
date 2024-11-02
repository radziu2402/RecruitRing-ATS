
package pl.pwr.recruitringcore.service;

import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.dto.RecruiterDTO;
import pl.pwr.recruitringcore.model.entities.Recruiter;
import pl.pwr.recruitringcore.repo.RecruiterRepository;

import java.util.List;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    private final RecruiterRepository recruiterRepository;

    public RecruiterServiceImpl(RecruiterRepository recruiterRepository) {
        this.recruiterRepository = recruiterRepository;
    }

    @Override
    public List<RecruiterDTO> findRecruitersByName(String query) {
        List<Recruiter> recruiters = recruiterRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);
        return recruiters.stream().map(this::mapToDTO).toList();
    }

    private RecruiterDTO mapToDTO(Recruiter recruiter) {
        return RecruiterDTO.builder()
                .id(recruiter.getId())
                .firstName(recruiter.getFirstName())
                .lastName(recruiter.getLastName())
                .position(recruiter.getPosition())
                .email(recruiter.getUser() != null ? recruiter.getUser().getEmail() : null)
                .build();
    }
}


