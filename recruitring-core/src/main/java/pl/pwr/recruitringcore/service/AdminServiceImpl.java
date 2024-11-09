package pl.pwr.recruitringcore.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pwr.recruitringcore.dto.AdminUserDTO;
import pl.pwr.recruitringcore.model.entities.JobPosting;
import pl.pwr.recruitringcore.model.entities.Recruiter;
import pl.pwr.recruitringcore.model.entities.User;
import pl.pwr.recruitringcore.model.enums.Role;
import pl.pwr.recruitringcore.repo.EventRepository;
import pl.pwr.recruitringcore.repo.JobRepository;
import pl.pwr.recruitringcore.repo.RecruiterRepository;
import pl.pwr.recruitringcore.repo.UserRepository;

import java.security.SecureRandom;
import java.util.List;


@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RecruiterRepository recruiterRepository;
    private final EventRepository eventRepository;
    private final JobRepository jobPostingRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailService;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public AdminServiceImpl(UserRepository userRepository, RecruiterRepository recruiterRepository, EventRepository eventRepository, JobRepository jobPostingRepository, PasswordEncoder passwordEncoder, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.recruiterRepository = recruiterRepository;
        this.eventRepository = eventRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public List<AdminUserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole().equals(Role.RECRUITER))
                .map(this::mapToAdminUserDTO)
                .toList();
    }

    @Override
    public AdminUserDTO addUser(AdminUserDTO adminUserDTO) {
        String generatedPassword = generateRandomPassword();
        User user = mapToUserEntity(adminUserDTO);
        user.setPassword(passwordEncoder.encode(generatedPassword));
        user.setRole(Role.RECRUITER);
        user.setLocked(false);

        Recruiter recruiter = new Recruiter();
        recruiter.setFirstName(adminUserDTO.getFirstName());
        recruiter.setLastName(adminUserDTO.getLastName());
        recruiter.setPosition(adminUserDTO.getPosition());
        recruiter.setDateOfBirth(adminUserDTO.getDateOfBirth());
        user.setRecruiter(recruiter);

        User savedUser = userRepository.save(user);
        recruiter.setUser(savedUser);

        recruiterRepository.save(recruiter);

        sendWelcomeEmail(adminUserDTO.getEmail(), generatedPassword);
        return mapToAdminUserDTO(savedUser);
    }

    protected String generateRandomPassword() {
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

    private void sendWelcomeEmail(String email, String generatedPassword) {
        String emailContent =
                "<!DOCTYPE html>" +
                        "<html lang=\"pl\">" +
                        "<head>" +
                        "  <meta charset=\"UTF-8\">" +
                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                        "  <style>" +
                        "    body { font-family: Arial, sans-serif; background-color: #f4f4f4; }" +
                        "    .container { max-width: 600px; margin: 40px auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                        "    .header { text-align: center; background-color: #007bff; color: #fff; padding: 10px; border-radius: 8px 8px 0 0; }" +
                        "    .header h1 { font-size: 24px; margin: 0; }" +
                        "    .content { padding: 20px; font-size: 16px; color: #333; }" +
                        "    .content p { line-height: 1.6; }" +
                        "    .footer { text-align: center; padding: 10px; font-size: 12px; color: #999; border-top: 1px solid #ddd; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class=\"container\">" +
                        "    <div class=\"header\">" +
                        "      <h1>Witaj w RecruitRing!</h1>" +
                        "    </div>" +
                        "    <div class=\"content\">" +
                        "      <p>Witaj,</p>" +
                        "      <p>Twoje konto zostało utworzone. Możesz się zalogować, używając poniższego hasła:</p>" +
                        "      <p><strong>Hasło: " + generatedPassword + "</strong></p>" +
                        "      <p>Zalecamy zmianę hasła po pierwszym zalogowaniu.</p>" +
                        "      <p>Serdecznie pozdrawiamy,<br>Zespół RecruitRing</p>" +
                        "    </div>" +
                        "    <div class=\"footer\">" +
                        "      <p>&copy; 2024 RecruitRing. Wszelkie prawa zastrzeżone.</p>" +
                        "    </div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";

        emailService.sendEmail(email, "Twoje konto w RecruitRing", emailContent);
    }


    @Override
    public void updateStatus(Long id, Boolean isLocked) {
        userRepository.findById(id).ifPresent(user -> {
            user.setLocked(isLocked);
            userRepository.save(user);
        });
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        Recruiter recruiter = recruiterRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono rekrutera dla podanego użytkownika"));

        eventRepository.deleteAllByRecruiter(recruiter);

        List<JobPosting> jobPostings = jobPostingRepository.findAllByRecruitersContains(recruiter);
        jobPostings.forEach(jobPosting -> {
            jobPosting.getRecruiters().remove(recruiter);
            jobPostingRepository.save(jobPosting);
        });

        recruiterRepository.delete(recruiter);

        userRepository.deleteById(userId);
    }


    private AdminUserDTO mapToAdminUserDTO(User user) {
        return AdminUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .firstName(user.getRecruiter().getFirstName())
                .lastName(user.getRecruiter().getLastName())
                .role(user.getRole())
                .dateOfBirth(user.getRecruiter().getDateOfBirth())
                .isLocked(user.isLocked())
                .position(user.getRecruiter().getPosition())
                .build();
    }

    private User mapToUserEntity(AdminUserDTO adminUserDTO) {
        return User.builder()
                .id(adminUserDTO.getId())
                .email(adminUserDTO.getEmail())
                .login(adminUserDTO.getLogin())
                .role(adminUserDTO.getRole())
                .isLocked(adminUserDTO.isLocked())
                .build();
    }
}
