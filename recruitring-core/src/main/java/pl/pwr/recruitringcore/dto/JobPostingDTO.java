package pl.pwr.recruitringcore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pwr.recruitringcore.model.enums.WorkType;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostingDTO {

    private Long id;
    private String title;
    private String description;
    private List<String> requirements;
    private String location;
    private WorkType workType;
    private LocalDate createdAt;
    private List<String> recruiters;
    private String jobCategory;
}
