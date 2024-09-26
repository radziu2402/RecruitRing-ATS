package pl.pwr.recruitringcore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pwr.recruitringcore.model.enums.WorkType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostingCreationDTO {
    private Long titleId;
    private Long locationId;
    private Long jobCategoryId;
    private List<Long> requirementIds;
    private List<Long> recruiterIds;
    private WorkType workType;
    private String description;
}
