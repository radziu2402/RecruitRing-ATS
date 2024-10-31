package pl.pwr.recruitringcore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateDTO {
    private UUID applicationCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String status;
    private Integer rating;
    private String city;
}
