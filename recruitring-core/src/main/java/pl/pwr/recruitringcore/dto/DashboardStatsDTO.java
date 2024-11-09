package pl.pwr.recruitringcore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO {
    private int openRecruitments;
    private int newCandidates;
    private int scheduledMeetings;
    private int avgTimeToHire;
    private int candidateRatings;
    private int rejectedCandidates;
    private int hiredCandidates;
}
