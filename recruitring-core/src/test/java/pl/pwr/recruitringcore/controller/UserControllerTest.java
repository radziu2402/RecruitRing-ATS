package pl.pwr.recruitringcore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.pwr.recruitringcore.dto.ProfileDataDTO;
import pl.pwr.recruitringcore.dto.UserDTO;
import pl.pwr.recruitringcore.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        userDTO = UserDTO.builder().login("user@example.com").build();
    }

    @Test
    void shouldGetProfileData() throws Exception {
        ProfileDataDTO profileData = ProfileDataDTO.builder().firstName("John").lastName("Doe").build();
        when(userService.getProfileData(any(UserDTO.class))).thenReturn(profileData);

        mockMvc.perform(get("/api/v1/profile")
                        .principal(() -> userDTO.getLogin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(userService, times(1)).getProfileData(any(UserDTO.class));
    }

    @Test
    void shouldUpdateProfileData() throws Exception {
        ProfileDataDTO updatedProfileData = ProfileDataDTO.builder().firstName("Updated").lastName("User").success(true).build();
        when(userService.updateProfileData(any(UserDTO.class), any(ProfileDataDTO.class)))
                .thenReturn(updatedProfileData);

        mockMvc.perform(post("/api/v1/profile")
                        .principal(() -> userDTO.getLogin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Updated\", \"lastName\": \"User\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("User"));

        verify(userService, times(1)).updateProfileData(any(UserDTO.class), any(ProfileDataDTO.class));
    }

    @Test
    void shouldReturnBadRequestOnProfileUpdate() throws Exception {
        when(userService.updateProfileData(any(UserDTO.class), any(ProfileDataDTO.class)))
                .thenReturn(ProfileDataDTO.builder().success(false).build());

        mockMvc.perform(post("/api/v1/profile")
                        .principal(() -> userDTO.getLogin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Updated\", \"lastName\": \"User\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Niepoprawne obecne haslo lub blad podczas aktualizacji profilu"));

        verify(userService, times(1)).updateProfileData(any(UserDTO.class), any(ProfileDataDTO.class));
    }

    @Test
    void shouldResetPassword() throws Exception {
        doNothing().when(userService).sendPasswordResetLink(any(String.class));

        mockMvc.perform(post("/api/v1/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"user@example.com\"}"))
                .andExpect(status().isOk());

        verify(userService, times(1)).sendPasswordResetLink(any(String.class));
    }

    @Test
    void shouldConfirmPasswordReset() throws Exception {
        when(userService.resetPassword(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(post("/api/v1/reset-password/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\": \"token123\", \"newPassword\": \"newPassword\"}"))
                .andExpect(status().isOk());

        verify(userService, times(1)).resetPassword(anyString(), anyString());
    }

    @Test
    void shouldReturnBadRequestOnPasswordReset() throws Exception {
        when(userService.resetPassword(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/api/v1/reset-password/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\": \"token123\", \"newPassword\": \"newPassword\"}"))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).resetPassword(anyString(), anyString());
    }

    @Test
    void shouldVerifyResetToken() throws Exception {
        when(userService.isResetTokenValid(anyString())).thenReturn(true);

        mockMvc.perform(get("/api/v1/reset-password/verify-token")
                        .param("token", "token123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(userService, times(1)).isResetTokenValid(anyString());
    }

    @Test
    void shouldReturnUnauthorizedOnInvalidResetToken() throws Exception {
        when(userService.isResetTokenValid(anyString())).thenReturn(false);

        mockMvc.perform(get("/api/v1/reset-password/verify-token")
                        .param("token", "invalidToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value(false));

        verify(userService, times(1)).isResetTokenValid(anyString());
    }
}
