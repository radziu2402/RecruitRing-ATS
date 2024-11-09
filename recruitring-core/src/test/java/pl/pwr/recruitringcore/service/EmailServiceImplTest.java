package pl.pwr.recruitringcore.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void shouldSendEmailSuccessfully() throws MessagingException {
        // GIVEN
        String to = "test@example.com";
        String subject = "Test Subject";
        String content = "Test Content";

        // Capture the MimeMessage for validation
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // WHEN
        emailService.sendEmail(to, subject, content);

        // THEN
        verify(mailSender, times(1)).send(mimeMessage);

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setText(content, true);
        helper.setTo(to);
        helper.setSubject(subject);
    }

    @Test
    void shouldLogErrorWhenMessagingExceptionOccurs() {
        // GIVEN
        String to = "test@example.com";
        String subject = "Test Subject";
        String content = "Test Content";

        doThrow(new RuntimeException("Failed to send")).when(mailSender).send(any(MimeMessage.class));

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> emailService.sendEmail(to, subject, content));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        assertThrows(IllegalArgumentException.class, () -> emailService.sendEmail(null, "Test Subject", "Test Content"));
    }

    @Test
    void shouldThrowExceptionWhenSubjectIsNull() {
        assertThrows(IllegalArgumentException.class, () -> emailService.sendEmail("test@example.com", null, "Test Content"));
    }

    @Test
    void shouldThrowExceptionWhenContentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> emailService.sendEmail("test@example.com", "Test Subject", null));
    }
}
