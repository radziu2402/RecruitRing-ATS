package pl.pwr.recruitringcore.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetConfirmDTO {
    private String token;
    private String newPassword;
}
