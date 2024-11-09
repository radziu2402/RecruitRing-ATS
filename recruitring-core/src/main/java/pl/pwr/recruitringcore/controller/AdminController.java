package pl.pwr.recruitringcore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.AdminApi;
import pl.pwr.recruitringcore.dto.AdminUserDTO;
import pl.pwr.recruitringcore.service.AdminService;

import java.util.List;

@RestController
public class AdminController implements AdminApi {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public List<AdminUserDTO> getAllUsers() {
        return adminService.getAllUsers();
    }

    @Override
    public ResponseEntity<AdminUserDTO> addUser(AdminUserDTO adminUserDTO) {
        AdminUserDTO newUser = adminService.addUser(adminUserDTO);
        return ResponseEntity.ok(newUser);
    }

    @Override
    public ResponseEntity<Void> updateUserStatus(Long id, Boolean isLocked) {
        adminService.updateStatus(id, isLocked);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
