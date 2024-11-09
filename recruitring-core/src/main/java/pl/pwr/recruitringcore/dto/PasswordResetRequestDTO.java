package pl.pwr.recruitringcore.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetRequestDTO {
    private String login;
}
