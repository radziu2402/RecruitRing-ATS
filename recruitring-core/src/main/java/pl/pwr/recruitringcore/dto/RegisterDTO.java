package pl.pwr.recruitringcore.dto;

import lombok.Data;
import pl.pwr.recruitringcore.model.enums.Role;

@Data
public class RegisterDTO {
    private String login;
    private String email;
    private String password;
    private Role role;
}