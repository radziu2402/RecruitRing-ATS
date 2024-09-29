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
public class JobPostingDTO {

    private Long id;
    private TitleDTO title;
    private String description;
    private UUID offerCode;
    private List<RequirementDTO> requirements;
    private LocationDTO location;
    private WorkType workType;
    private LocalDate createdAt;
    private List<RecruiterDTO> recruiters;
    private JobCategoryDTO jobCategory;
}
