package pl.pwr.recruitringcore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pwr.recruitringcore.dto.AdminUserDTO;
import pl.pwr.recruitringcore.model.entities.JobPosting;
import pl.pwr.recruitringcore.model.entities.Recruiter;
import pl.pwr.recruitringcore.model.entities.User;
import pl.pwr.recruitringcore.model.enums.Role;
import pl.pwr.recruitringcore.repo.EventRepository;
import pl.pwr.recruitringcore.repo.JobRepository;
import pl.pwr.recruitringcore.repo.RecruiterRepository;
import pl.pwr.recruitringcore.repo.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private JobRepository jobPostingRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailServiceImpl emailService;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void shouldReturnAllRecruiters() {
        // GIVEN
        User recruiter = new User();
        recruiter.setRole(Role.RECRUITER);
        recruiter.setRecruiter(new Recruiter());

        // WHEN
        when(userRepository.findAll()).thenReturn(List.of(recruiter));

        List<AdminUserDTO> result = adminService.getAllUsers();

        // THEN
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldAddUserAndSendWelcomeEmail() {
        // GIVEN
        AdminUserDTO adminUserDTO = AdminUserDTO.builder().email("test@example.com").build();
        User user = new User();
        Recruiter recruiter = new Recruiter();
        user.setRecruiter(recruiter);

        // WHEN
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        AdminUserDTO result = adminService.addUser(adminUserDTO);

        // THEN
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendEmail(eq("test@example.com"), anyString(), anyString());
    }

    @Test
    void shouldUpdateUserStatus() {
        // GIVEN
        User user = new User();

        // WHEN
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        adminService.updateStatus(1L, true);

        // THEN
        assertTrue(user.isLocked());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldDeleteUserAndCascadeRemoveDependencies() {
        // GIVEN
        Recruiter recruiter = new Recruiter();

        // WHEN
        when(recruiterRepository.findByUserId(anyLong())).thenReturn(Optional.of(recruiter));
        JobPosting jobPosting = new JobPosting();
        jobPosting.setRecruiters(new HashSet<>());
        jobPosting.getRecruiters().add(recruiter);
        when(jobPostingRepository.findAllByRecruitersContains(recruiter)).thenReturn(List.of(jobPosting));

        adminService.deleteUser(1L);

        // THEN
        verify(eventRepository, times(1)).deleteAllByRecruiter(recruiter);
        verify(jobPostingRepository, times(1)).save(jobPosting);
        verify(recruiterRepository, times(1)).delete(recruiter);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldGenerateRandomPassword() {
        // WHEN
        String password = adminService.generateRandomPassword();

        // THEN
        assertEquals(10, password.length());
    }
}
