package pl.pwr.recruitringcore.service;

import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.model.entities.VerificationCode;
import pl.pwr.recruitringcore.repo.VerificationCodeRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    private final Random random = new Random();
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailServiceImpl emailServiceImpl;

    public AuthServiceImpl(VerificationCodeRepository verificationCodeRepository, EmailServiceImpl emailServiceImpl) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.emailServiceImpl = emailServiceImpl;
    }

    @Override
    public void sendVerificationCode(String email) {
        String code = generateVerificationCode();

        VerificationCode verificationCode = VerificationCode.builder()
                .email(email)
                .code(code)
                .createdAt(LocalDateTime.now())
                .build();

        verificationCodeRepository.findByEmail(email).ifPresent(verificationCodeRepository::delete);

        verificationCodeRepository.save(verificationCode);

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
                        "    .footer { text-align: center; padding: 10px; font-size: 12px; color: #999; border-top: 1px solid #ddd; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class=\"container\">" +
                        "    <div class=\"header\">" +
                        "      <h1>Weryfikacja adresu e-mail</h1>" +
                        "    </div>" +
                        "    <div class=\"content\">" +
                        "      <p>Witaj,</p>" +
                        "      <p>Dziękujemy za aplikowanie na wybrane stanowisko. Aby zakończyć proces aplikacji, wprowadź poniższy kod weryfikacyjny:</p>" +
                        "      <p><strong>Kod weryfikacyjny: " + code + "</strong></p>" +
                        "      <p>Ten kod jest ważny przez 10 minut. Jeśli nie składałeś wniosku o aplikację, zignoruj tę wiadomość.</p>" +
                        "      <p>Z poważaniem,<br>Zespół Rekrutacji</p>" +
                        "    </div>" +
                        "    <div class=\"footer\">" +
                        "      <p>&copy; 2024 RecruitRing. Wszelkie prawa zastrzeżone.</p>" +
                        "    </div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";

        emailServiceImpl.sendEmail(email, "Verification Code", emailContent);
    }

    @Override
    public boolean verifyCode(String email, String code) {
        Optional<VerificationCode> optionalCode = verificationCodeRepository.findByEmail(email);

        return optionalCode.map(storedCode -> {
            if (storedCode.getCode().equals(code) &&
                    storedCode.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(10)) &&
                    storedCode.getUsedAt() == null) {
                verificationCodeRepository.delete(storedCode);
                return true;
            }
            return false;
        }).orElse(false);
    }


    private String generateVerificationCode() {
        return String.format("%06d", random.nextInt(999999));
    }
}
