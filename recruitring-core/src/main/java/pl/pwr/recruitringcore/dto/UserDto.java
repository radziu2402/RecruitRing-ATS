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
public class UserDto {
    @JsonIgnore
    private Integer id;
    private String email;
    private String login;
    private String role;
    @JsonIgnore
    private String token;
}
