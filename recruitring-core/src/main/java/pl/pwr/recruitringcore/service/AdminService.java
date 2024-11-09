package pl.pwr.recruitringcore.service;

import pl.pwr.recruitringcore.dto.AdminUserDTO;

import java.util.List;

public interface AdminService {

    List<AdminUserDTO> getAllUsers();

    AdminUserDTO addUser(AdminUserDTO adminUserDTO);

    void updateStatus(Long id, Boolean isLocked);

    void deleteUser(Long id);
}
