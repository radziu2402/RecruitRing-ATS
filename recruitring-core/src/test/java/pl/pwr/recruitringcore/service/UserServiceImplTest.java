package pl.pwr.recruitringcore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pwr.recruitringcore.dto.JwtResultDTO;
import pl.pwr.recruitringcore.dto.LoginDTO;
import pl.pwr.recruitringcore.exceptions.UnknownUserException;
import pl.pwr.recruitringcore.model.entities.PasswordResetToken;
import pl.pwr.recruitringcore.model.entities.User;
import pl.pwr.recruitringcore.repo.PasswordResetTokenRepository;
import pl.pwr.recruitringcore.repo.UserRepository;
import pl.pwr.recruitringcore.security.UserAuthenticationProvider;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserAuthenticationProvider userAuthenticationProvider;
    @Mock
    private PasswordResetTokenRepository tokenRepository;
    @Mock
    private EmailServiceImpl emailServiceImpl;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldLoginSuccessfully() {
        // GIVEN
        LoginDTO loginDTO = new LoginDTO("testUser", "testPassword");
        User user = new User();
        user.setLogin("testUser");
        user.setPassword("encodedPassword");
        user.setLocked(false);

        when(userRepository.findByLogin(loginDTO.getLogin())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(true);
        when(userAuthenticationProvider.createToken(user)).thenReturn("jwtToken");

        // WHEN
        JwtResultDTO result = userService.login(loginDTO);

        // THEN
        assertTrue(result.isSuccess());
        assertEquals("jwtToken", result.getAccessToken());
    }

    @Test
    void shouldThrowUnknownUserExceptionWhenUserNotFound() {
        // GIVEN
        LoginDTO loginDTO = new LoginDTO("unknownUser", "testPassword");
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        // WHEN THEN
        assertThrows(UnknownUserException.class, () -> userService.login(loginDTO));
    }

    @Test
    void shouldSendPasswordResetLinkSuccessfully() {
        // GIVEN
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(new PasswordResetToken());

        // WHEN
        userService.sendPasswordResetLink(email);

        // THEN
        verify(emailServiceImpl, times(1)).sendEmail(eq(email), anyString(), contains("/set-new-password?token="));
    }

    @Test
    void shouldResetPasswordSuccessfully() {
        // GIVEN
        String token = UUID.randomUUID().toString();
        String newPassword = "newPassword";
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        User user = new User();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpirationDate(LocalDateTime.now().plusMinutes(5));
        passwordResetToken.setUsed(false);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(passwordResetToken));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        // WHEN
        boolean result = userService.resetPassword(token, newPassword);

        // THEN
        assertTrue(result);
        assertEquals("encodedNewPassword", user.getPassword());
        assertTrue(passwordResetToken.isUsed());
        verify(tokenRepository, times(1)).save(passwordResetToken);
    }

    @Test
    void shouldReturnFalseWhenResetTokenIsInvalidOrExpired() {
        // GIVEN
        String token = UUID.randomUUID().toString();
        when(tokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // WHEN
        boolean result = userService.isResetTokenValid(token);

        // THEN
        assertFalse(result);
    }
}
