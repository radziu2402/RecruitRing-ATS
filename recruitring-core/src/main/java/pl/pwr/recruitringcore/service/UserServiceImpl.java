package pl.pwr.recruitringcore.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.dto.*;
import pl.pwr.recruitringcore.exceptions.UnknownUserException;
import pl.pwr.recruitringcore.exceptions.UserAlreadyExistsException;
import pl.pwr.recruitringcore.factory.UserDataProducerFactory;
import pl.pwr.recruitringcore.model.entities.User;
import pl.pwr.recruitringcore.model.enums.Role;
import pl.pwr.recruitringcore.producer.UserDataProducer;
import pl.pwr.recruitringcore.repo.UserRepository;
import pl.pwr.recruitringcore.security.UserAuthenticationProvider;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final UserDataProducerFactory userDataProducerFactory;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserAuthenticationProvider userAuthenticationProvider, UserDataProducerFactory userDataProducerFactory) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.userDataProducerFactory = userDataProducerFactory;
    }

    @Override
    public UserDTO register(RegisterDTO registerDto) {
        if (userRepository.existsByLogin(registerDto.getLogin())) {
            throw new UserAlreadyExistsException("User with this login already exists", HttpStatus.CONFLICT);
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists", HttpStatus.CONFLICT);
        }

        User newUser = User.builder()
                .login(registerDto.getLogin())
                .email(registerDto.getEmail())
                .role(registerDto.getRole())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .build();

        userRepository.save(newUser);

        return toUserDto(newUser);
    }

    private UserDTO toUserDto(User newUser) {
        return UserDTO.builder()
                .id(newUser.getId())
                .login(newUser.getLogin())
                .email(newUser.getEmail())
                .role(newUser.getRole().name())
                .build();
    }

    @Override
    public JwtResultDTO login(LoginDTO credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new UnknownUserException("No user found", HttpStatus.BAD_REQUEST));

        if (!passwordEncoder.matches(credentialsDto.getPassword(), user.getPassword())) {
            return JwtResultDTO.builder().success(false).build();
        }

        String token = userAuthenticationProvider.createToken(user);
        return JwtResultDTO.builder().accessToken(token).success(true).build();
    }

    @Override
    public UserDTO findUserByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UnknownUserException("Unknown user", HttpStatus.BAD_REQUEST));
        return toUserDto(user);
    }

    @Override
    public UserDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UnknownUserException("Unknown user", HttpStatus.BAD_REQUEST));
        return toUserDto(user);
    }

    @Override
    public ProfileDataDTO getProfileData(UserDTO userDto) {
        UserDataProducer producer = userDataProducerFactory.get(Role.valueOf(userDto.getRole()));
        return producer.buildUserData(userDto);
    }

    @Override
    @Transactional
    public ProfileDataDTO updateProfileData(UserDTO userDto, ProfileDataDTO profileDataDTO) {
        Optional<User> userOptional = userRepository.findById(userDto.getId());
        if (userOptional.isEmpty()) {
            return ProfileDataDTO.builder().success(false).build();
        }
        User user = userOptional.get();

        user.setEmail(profileDataDTO.getEmail());
        user.setLogin(profileDataDTO.getLogin());
        user.setPassword(passwordEncoder.encode(profileDataDTO.getPassword()));
        userRepository.save(user);
        return profileDataDTO;
    }
}
