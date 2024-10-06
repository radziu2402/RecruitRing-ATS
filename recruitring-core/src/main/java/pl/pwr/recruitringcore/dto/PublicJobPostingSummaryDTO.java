package pl.pwr.recruitringcore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pwr.recruitringcore.model.enums.WorkType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicJobPostingSummaryDTO {

    private Long id;
    private String title;
    private UUID offerCode;
    private String location;
    private WorkType workType;
    private LocalDate createdAt;
    private List<String> recruiters;
    private String jobCategory;
}
