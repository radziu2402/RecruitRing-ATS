package pl.pwr.recruitringcore.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LoginDTO {

    private String login;

    private String password;
}
