package pl.pwr.recruitringcore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingEmailRequestDTO {
    private Long eventId;
    private String candidateEmail;
}