package pl.pwr.recruitringcore.producer;

import org.springframework.stereotype.Component;
import pl.pwr.recruitringcore.dto.ProfileDataDTO;
import pl.pwr.recruitringcore.dto.UserDTO;
import pl.pwr.recruitringcore.model.entities.Recruiter;
import pl.pwr.recruitringcore.model.entities.User;
import pl.pwr.recruitringcore.model.enums.Role;
import pl.pwr.recruitringcore.repo.RecruiterRepository;
import pl.pwr.recruitringcore.repo.UserRepository;

import java.util.Optional;

@Component
public class RecruiterDataProducer implements UserDataProducer {

    private final UserRepository userRepository;
    private final RecruiterRepository recruiterRepository;

    public RecruiterDataProducer(UserRepository userRepository, RecruiterRepository recruiterRepository) {
        this.userRepository = userRepository;
        this.recruiterRepository = recruiterRepository;
    }

    @Override
    public ProfileDataDTO buildUserData(UserDTO userDto) {
        Optional<User> userOptional = userRepository.findById(userDto.getId());
        if (userOptional.isEmpty()) {
            return ProfileDataDTO.builder().success(false).build();
        }
        User user = userOptional.get();

        Optional<Recruiter> recruiterOptional = recruiterRepository.findById(userDto.getId());
        if (recruiterOptional.isEmpty()) {
            return ProfileDataDTO.builder().success(false).build();
        }
        Recruiter recruiter = recruiterOptional.get();

        return buildProfileDataDto(user, recruiter);
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.RECRUITER;
    }

    private ProfileDataDTO buildProfileDataDto(User user, Recruiter recruiter) {
        return ProfileDataDTO.builder()
                .firstName(recruiter.getFirstName())
                .lastName(recruiter.getLastName())
                .position(recruiter.getPosition())
                .dateOfBirth(recruiter.getDateOfBirth().toString())
                .email(user.getEmail())
                .login(user.getLogin())
                .success(true)
                .build();
    }
}
