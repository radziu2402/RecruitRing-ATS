package pl.pwr.recruitringcore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.pwr.recruitringcore.dto.PublicJobPostingDTO;
import pl.pwr.recruitringcore.dto.PublicJobPostingSummaryDTO;
import pl.pwr.recruitringcore.model.enums.WorkType;

import java.util.UUID;

public interface PublicJobService {
    Page<PublicJobPostingSummaryDTO> getAllJobs(PageRequest pageRequest, Long titleId, Long locationId, Long jobCategoryId, WorkType workType);

    PublicJobPostingDTO getJobByOfferCode(UUID offerCode);
}
