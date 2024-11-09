package pl.pwr.recruitringcore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.pwr.recruitringcore.dto.JwtResultDTO;
import pl.pwr.recruitringcore.dto.LoginDTO;
import pl.pwr.recruitringcore.exceptions.UserAlreadyExistsException;
import pl.pwr.recruitringcore.service.AuthService;
import pl.pwr.recruitringcore.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        JwtResultDTO jwtResult = new JwtResultDTO("sample.jwt.token", true);

        when(userService.login(any(LoginDTO.class))).thenReturn(jwtResult);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk());

        verify(userService, times(1)).login(any(LoginDTO.class));
    }

    @Test
    void shouldReturnBadRequestWhenLoginFails() throws Exception {
        JwtResultDTO jwtResult = new JwtResultDTO(null, false);

        when(userService.login(any(LoginDTO.class))).thenReturn(jwtResult);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\", \"password\":\"wrongpassword\"}"))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).login(any(LoginDTO.class));
    }

    @Test
    void shouldVerifyEmail() throws Exception {
        String email = "user@example.com";

        doNothing().when(authService).sendVerificationCode(email);

        mockMvc.perform(post("/api/v1/verify-email")
                        .param("email", email))
                .andExpect(status().isOk());

        verify(authService, times(1)).sendVerificationCode(email);
    }

    @Test
    void shouldConfirmVerificationCode() throws Exception {
        String email = "user@example.com";
        String code = "123456";

        when(authService.verifyCode(email, code)).thenReturn(true);

        mockMvc.perform(post("/api/v1/confirm-code")
                        .param("email", email)
                        .param("code", code))
                .andExpect(status().isOk());

        verify(authService, times(1)).verifyCode(email, code);
    }

    @Test
    void shouldReturnBadRequestWhenVerificationFails() throws Exception {
        String email = "user@example.com";
        String code = "wrongcode";

        when(authService.verifyCode(email, code)).thenReturn(false);

        mockMvc.perform(post("/api/v1/confirm-code")
                        .param("email", email)
                        .param("code", code))
                .andExpect(status().isBadRequest());

        verify(authService, times(1)).verifyCode(email, code);
    }

    @Test
    void shouldHandleUserAlreadyExistsException() throws Exception {
        doThrow(new UserAlreadyExistsException("User already exists", HttpStatus.CONFLICT)).when(authService).sendVerificationCode(anyString());

        mockMvc.perform(post("/api/v1/verify-email")
                        .param("email", "existinguser@example.com"))
                .andExpect(status().isConflict());

        verify(authService, times(1)).sendVerificationCode(anyString());
    }
}
