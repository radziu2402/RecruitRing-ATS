package pl.pwr.recruitringcore.api;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pwr.recruitringcore.dto.PublicJobPostingDTO;
import pl.pwr.recruitringcore.dto.PublicJobPostingSummaryDTO;
import pl.pwr.recruitringcore.model.enums.WorkType;

import java.util.UUID;

@RequestMapping("api/v1/public-jobs")
public interface PublicJobApi {

    @GetMapping
    ResponseEntity<Page<PublicJobPostingSummaryDTO>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long titleId,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) Long jobCategoryId,
            @RequestParam(required = false) WorkType workType);

    @GetMapping("/{offerCode}")
    ResponseEntity<PublicJobPostingDTO> getJobByOfferCode(@PathVariable UUID offerCode);
}