package pl.pwr.recruitringcore.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResultDto {

    private String accessToken;

    @JsonIgnore
    private boolean success;
}
