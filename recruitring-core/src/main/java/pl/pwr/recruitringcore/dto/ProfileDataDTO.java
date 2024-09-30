package pl.pwr.recruitringcore.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDataDTO {
    private String login;
    private String firstName;
    private String lastName;
    private String position;
    private String password;
    private String email;
    private String dateOfBirth;

    @JsonIgnore
    boolean success;
}
