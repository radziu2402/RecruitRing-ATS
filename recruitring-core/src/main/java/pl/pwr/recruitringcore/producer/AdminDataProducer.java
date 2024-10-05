package pl.pwr.recruitringcore.producer;

import org.springframework.stereotype.Component;
import pl.pwr.recruitringcore.dto.ProfileDataDTO;
import pl.pwr.recruitringcore.dto.UserDTO;
import pl.pwr.recruitringcore.model.entities.User;
import pl.pwr.recruitringcore.model.enums.Role;
import pl.pwr.recruitringcore.repo.UserRepository;

import java.util.Optional;

@Component
public class AdminDataProducer implements UserDataProducer {

    private static final String ADMIN = "ADMIN";

    private final UserRepository userRepository;

    public AdminDataProducer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ProfileDataDTO buildUserData(UserDTO userDto) {
        Optional<User> userOptional = userRepository.findById(userDto.getId());
        if (userOptional.isEmpty()) {
            return ProfileDataDTO.builder().success(false).build();
        }
        User user = userOptional.get();
        return ProfileDataDTO.builder()
                .firstName(ADMIN)
                .lastName(ADMIN)
                .position(ADMIN)
                .dateOfBirth(ADMIN)
                .email(user.getEmail())
                .login(user.getLogin())
                .success(true)
                .build();
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.ADMINISTRATOR;
    }
}
