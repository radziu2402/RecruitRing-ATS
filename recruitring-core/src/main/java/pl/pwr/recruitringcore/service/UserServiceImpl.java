package pl.pwr.recruitringcore.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.dto.*;
import pl.pwr.recruitringcore.exceptions.UnknownUserException;
import pl.pwr.recruitringcore.exceptions.UserAlreadyExistsException;
import pl.pwr.recruitringcore.factory.UserDataProducerFactory;
import pl.pwr.recruitringcore.model.entities.PasswordResetToken;
import pl.pwr.recruitringcore.model.entities.User;
import pl.pwr.recruitringcore.model.enums.Role;
import pl.pwr.recruitringcore.producer.UserDataProducer;
import pl.pwr.recruitringcore.repo.PasswordResetTokenRepository;
import pl.pwr.recruitringcore.repo.UserRepository;
import pl.pwr.recruitringcore.security.UserAuthenticationProvider;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Value("${cors.allowedOrigins}")
    private String frontEndUrl;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final UserDataProducerFactory userDataProducerFactory;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailServiceImpl emailServiceImpl;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserAuthenticationProvider userAuthenticationProvider, UserDataProducerFactory userDataProducerFactory, PasswordResetTokenRepository tokenRepository, EmailServiceImpl emailServiceImpl) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.userDataProducerFactory = userDataProducerFactory;
        this.tokenRepository = tokenRepository;
        this.emailServiceImpl = emailServiceImpl;
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

        if (profileDataDTO.getOldPassword() != null && !profileDataDTO.getOldPassword().isEmpty()) {
            if (!passwordEncoder.matches(profileDataDTO.getOldPassword(), user.getPassword())) {
                return ProfileDataDTO.builder().success(false).build();
            }
            if (profileDataDTO.getNewPassword() != null && !profileDataDTO.getNewPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(profileDataDTO.getNewPassword()));
            }
        }
        userRepository.save(user);
        profileDataDTO.setSuccess(true);
        return profileDataDTO;
    }

    @Override
    public void sendPasswordResetLink(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return;
        }

        User user = optionalUser.get();

        String token = UUID.randomUUID().toString();
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(10);

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpirationDate(expirationDate);
        passwordResetToken.setCreatedAt(LocalDateTime.now());
        passwordResetToken.setUsed(false);

        tokenRepository.save(passwordResetToken);

        String resetUrl = frontEndUrl + "/set-new-password?token=" + token;

        String emailContent =
                "<!DOCTYPE html>" +
                        "<html lang=\"pl\">" +
                        "<head>" +
                        "  <meta charset=\"UTF-8\">" +
                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                        "  <style>" +
                        "    body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }" +
                        "    .container { max-width: 600px; margin: 40px auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                        "    .header { text-align: center; padding: 10px 0; background-color: #007bff; color: #fff; border-radius: 8px 8px 0 0; }" +
                        "    .header h1 { margin: 0; font-size: 24px; }" +
                        "    .content { padding: 20px; font-size: 16px; color: #333; }" +
                        "    .content p { margin: 0 0 20px; line-height: 1.6; }" +
                        "    .content .note { margin-top: 30px; }" +
                        "    .content a { display: inline-block; padding: 10px 20px; background-color: #007bff; color: #fff; text-decoration: none; border-radius: 4px; font-weight: bold; }" +
                        "    .content a:hover { background-color: #0056b3; }" +
                        "    .footer { text-align: center; padding: 10px; font-size: 12px; color: #999; border-top: 1px solid #ddd; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class=\"container\">" +
                        "    <div class=\"header\">" +
                        "      <h1>Resetowanie hasła</h1>" +
                        "    </div>" +
                        "    <div class=\"content\">" +
                        "      <p>Witaj,</p>" +
                        "      <p>Otrzymaliśmy prośbę o zresetowanie hasła do Twojego konta.</p>" +
                        "      <p>Kliknij w poniższy przycisk, aby utworzyć nowe hasło:</p>" +
                        "      <a href=\"" + resetUrl + "\">Resetuj hasło</a>" +
                        "      <p class=\"note\">Jeśli to NIE TY złożyłeś prośbę o zresetowanie hasła, prosimy o jak najszybszy kontakt.</p>" +
                        "      <p>Z poważaniem,<br>Zespół Wsparcia</p>" +
                        "    </div>" +
                        "    <div class=\"footer\">" +
                        "      <p>&copy; 2024 RecruitRing. Wszelkie prawa zastrzeżone.</p>" +
                        "    </div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";

        emailServiceImpl.sendEmail(email, "Reset your password", emailContent);
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (passwordResetToken.getExpirationDate().isAfter(LocalDateTime.now()) && !passwordResetToken.isUsed()) {
            User user = passwordResetToken.getUser();

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            passwordResetToken.setUsed(true);
            tokenRepository.save(passwordResetToken);

            return true;
        }
        return false;
    }

    @Override
    public boolean isResetTokenValid(String token) {
        Optional<PasswordResetToken> resetTokenOpt = tokenRepository.findByToken(token);

        if (resetTokenOpt.isEmpty()) {
            return false;
        }

        PasswordResetToken resetToken = resetTokenOpt.get();
        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        return !resetToken.isUsed();
    }

}
