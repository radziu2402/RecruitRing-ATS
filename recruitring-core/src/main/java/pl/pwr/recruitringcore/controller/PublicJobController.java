package pl.pwr.recruitringcore.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.PublicJobApi;
import pl.pwr.recruitringcore.dto.PublicJobPostingDTO;
import pl.pwr.recruitringcore.dto.PublicJobPostingSummaryDTO;
import pl.pwr.recruitringcore.model.enums.WorkType;
import pl.pwr.recruitringcore.service.PublicJobService;

import java.util.UUID;

@RestController
public class PublicJobController implements PublicJobApi {

    private final PublicJobService publicJobService;

    public PublicJobController(PublicJobService publicJobService) {
        this.publicJobService = publicJobService;
    }

    @Override
    public ResponseEntity<Page<PublicJobPostingSummaryDTO>> getAllJobs(int page, int size, Long titleId, Long locationId, Long jobCategoryId, WorkType workType) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PublicJobPostingSummaryDTO> jobPostings = publicJobService.getAllJobs(pageRequest, titleId, locationId, jobCategoryId, workType);
        return ResponseEntity.ok(jobPostings);
    }

    @Override
    public ResponseEntity<PublicJobPostingDTO> getJobByOfferCode(UUID offerCode) {
        PublicJobPostingDTO jobPosting = publicJobService.getJobByOfferCode(offerCode);
        return ResponseEntity.ok(jobPosting);
    }
}
