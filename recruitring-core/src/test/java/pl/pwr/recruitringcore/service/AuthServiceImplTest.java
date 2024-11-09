package pl.pwr.recruitringcore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pwr.recruitringcore.model.entities.VerificationCode;
import pl.pwr.recruitringcore.repo.VerificationCodeRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @Mock
    private EmailServiceImpl emailServiceImpl;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldSendVerificationCode() {
        // GIVEN
        String email = "test@example.com";
        VerificationCode existingCode = VerificationCode.builder()
                .email(email)
                .code("123456")
                .createdAt(LocalDateTime.now().minusMinutes(5))
                .build();

        when(verificationCodeRepository.findByEmail(email)).thenReturn(Optional.of(existingCode));

        // WHEN
        authService.sendVerificationCode(email);

        // THEN
        verify(verificationCodeRepository, times(1)).delete(existingCode);
        verify(verificationCodeRepository, times(1)).save(any(VerificationCode.class));
        verify(emailServiceImpl, times(1)).sendEmail(eq(email), eq("Verification Code"), anyString());
    }

    @Test
    void shouldVerifyCodeSuccessfully() {
        // GIVEN
        String email = "test@example.com";
        String code = "123456";
        VerificationCode verificationCode = VerificationCode.builder()
                .email(email)
                .code(code)
                .createdAt(LocalDateTime.now().minusMinutes(5))
                .build();

        when(verificationCodeRepository.findByEmail(email)).thenReturn(Optional.of(verificationCode));

        // WHEN
        boolean result = authService.verifyCode(email, code);

        // THEN
        assertTrue(result);
        verify(verificationCodeRepository, times(1)).delete(verificationCode);
    }

    @Test
    void shouldNotVerifyCodeWhenCodeIsIncorrect() {
        // GIVEN
        String email = "test@example.com";
        String correctCode = "123456";
        String incorrectCode = "654321";
        VerificationCode verificationCode = VerificationCode.builder()
                .email(email)
                .code(correctCode)
                .createdAt(LocalDateTime.now().minusMinutes(5))
                .build();

        when(verificationCodeRepository.findByEmail(email)).thenReturn(Optional.of(verificationCode));

        // WHEN
        boolean result = authService.verifyCode(email, incorrectCode);

        // THEN
        assertFalse(result);
        verify(verificationCodeRepository, never()).delete(verificationCode);
    }

    @Test
    void shouldNotVerifyCodeWhenCodeIsExpired() {
        // GIVEN
        String email = "test@example.com";
        String code = "123456";
        VerificationCode expiredCode = VerificationCode.builder()
                .email(email)
                .code(code)
                .createdAt(LocalDateTime.now().minusMinutes(15))
                .build();

        when(verificationCodeRepository.findByEmail(email)).thenReturn(Optional.of(expiredCode));

        // WHEN
        boolean result = authService.verifyCode(email, code);

        // THEN
        assertFalse(result);
        verify(verificationCodeRepository, never()).delete(expiredCode);
    }
}
