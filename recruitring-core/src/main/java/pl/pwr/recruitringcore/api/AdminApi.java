package pl.pwr.recruitringcore.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.recruitringcore.dto.AdminUserDTO;

import java.util.List;

@RequestMapping("/api/v1/admin")
public interface AdminApi {

    @GetMapping("/users")
    List<AdminUserDTO> getAllUsers();

    @PostMapping("/users")
    ResponseEntity<AdminUserDTO> addUser(@RequestBody AdminUserDTO adminUserDTO);

    @PatchMapping("/users/{id}/status")
    ResponseEntity<Void> updateUserStatus(@PathVariable Long id, @RequestBody Boolean isLocked);

    @DeleteMapping("/users/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id);
}
