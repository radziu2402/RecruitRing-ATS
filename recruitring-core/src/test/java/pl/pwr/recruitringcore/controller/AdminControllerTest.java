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
import pl.pwr.recruitringcore.dto.AdminUserDTO;
import pl.pwr.recruitringcore.service.AdminService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        List<AdminUserDTO> users = Arrays.asList(
                AdminUserDTO.builder().id(1L).login("user1").email("user1@example.com").isLocked(false).build(),
                AdminUserDTO.builder().id(2L).login("user2").email("user2@example.com").isLocked(true).build()
        );
        when(adminService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].login").value("user1"))
                .andExpect(jsonPath("$[1].locked").value(true));

        verify(adminService, times(1)).getAllUsers();
    }

    @Test
    void shouldAddUser() throws Exception {
        AdminUserDTO createdUser = AdminUserDTO.builder().id(3L).login("newUser").email("newUser@example.com").isLocked(false).build();

        when(adminService.addUser(any(AdminUserDTO.class))).thenReturn(createdUser);

        mockMvc.perform(post("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"newUser\",\"email\":\"newUser@example.com\",\"isLocked\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("newUser"))
                .andExpect(jsonPath("$.locked").value(false));

        verify(adminService, times(1)).addUser(any(AdminUserDTO.class));
    }

    @Test
    void shouldUpdateUserStatus() throws Exception {
        Long userId = 1L;
        Boolean isLocked = true;
        doNothing().when(adminService).updateStatus(userId, isLocked);

        mockMvc.perform(patch("/api/v1/admin/users/{id}/status", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("true"))
                .andExpect(status().isOk());

        verify(adminService, times(1)).updateStatus(userId, isLocked);
    }

    @Test
    void shouldDeleteUser() throws Exception {
        Long userId = 1L;
        doNothing().when(adminService).deleteUser(userId);

        mockMvc.perform(delete("/api/v1/admin/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(adminService, times(1)).deleteUser(userId);
    }
}
