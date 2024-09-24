package pl.pwr.recruitringcore.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.dto.*;
import pl.pwr.recruitringcore.exceptions.UnknownUserException;
import pl.pwr.recruitringcore.exceptions.UserAlreadyExistsException;
import pl.pwr.recruitringcore.factory.UserDataProducerFactory;
import pl.pwr.recruitringcore.mappers.UserMapper;
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
    private final UserMapper userMapper;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final UserDataProducerFactory userDataProducerFactory;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, UserAuthenticationProvider userAuthenticationProvider, UserDataProducerFactory userDataProducerFactory) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.userDataProducerFactory = userDataProducerFactory;
    }

    @Override
    public UserDto register(RegisterDTO registerDto) {
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

        return userMapper.toUserDto(newUser);
    }

    @Override
    public JwtResultDto login(LoginDTO credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new UnknownUserException("No user found", HttpStatus.BAD_REQUEST));

        if (!passwordEncoder.matches(credentialsDto.getPassword(), user.getPassword())) {
            return JwtResultDto.builder().success(false).build();
        }

        String token = userAuthenticationProvider.createToken(user);
        return JwtResultDto.builder().accessToken(token).success(true).build();
    }

    @Override
    public UserDto findUserByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UnknownUserException("Unknown user", HttpStatus.BAD_REQUEST));
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto findUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UnknownUserException("Unknown user", HttpStatus.BAD_REQUEST));
        return userMapper.toUserDto(user);
    }

    @Override
    public ProfileDataDto getProfileData(UserDto userDto) {
        UserDataProducer producer = userDataProducerFactory.get(Role.valueOf(userDto.getRole()));
        return producer.buildUserData(userDto);
    }

    @Override
    @Transactional
    public ProfileDataDto updateProfileData(UserDto userDto, ProfileDataDto profileDataDto) {
        Optional<User> userOptional = userRepository.findById(userDto.getId());
        if (userOptional.isEmpty()) {
            return ProfileDataDto.builder().success(false).build();
        }
        User user = userOptional.get();

        user.setEmail(profileDataDto.getEmail());
        user.setLogin(profileDataDto.getLogin());
        user.setPassword(profileDataDto.getPassword());
        userRepository.save(user);
        return profileDataDto;
    }
}
