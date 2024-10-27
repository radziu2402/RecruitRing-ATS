package pl.pwr.recruitringcore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pwr.recruitringcore.model.enums.WorkType;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterJobPostingDTO {

    private Long id;
    private UUID offerCode;
    private String title;
    private String location;
    private int totalApplications;
    private int newApplications;
    private String jobCategory;
    private WorkType workType;

}
