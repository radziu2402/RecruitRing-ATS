package pl.pwr.recruitringcore.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pwr.recruitringcore.dto.ApplicationDTO;
import pl.pwr.recruitringcore.dto.ApplicationStatusDTO;
import pl.pwr.recruitringcore.dto.CandidateDTO;
import pl.pwr.recruitringcore.dto.DetailedCandidateDTO;

import java.util.List;

@RequestMapping("api/v1/")
public interface ApplicationApi {

    @PostMapping(value = "apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> createApplication(
            @RequestPart("application") ApplicationDTO applicationDto,
            @RequestPart("cv") MultipartFile cvFile
    );

    @GetMapping("recruitments/{offerCode}/candidates")
    ResponseEntity<List<CandidateDTO>> getCandidatesByOfferCode(@PathVariable String offerCode);

    @GetMapping("recruitments/candidates/{applicationCode}")
    ResponseEntity<DetailedCandidateDTO> getCandidateDetails(@PathVariable String applicationCode);

    @GetMapping("documents/download-url/{blobName}")
    ResponseEntity<String> getFileUrlToDownload(@PathVariable String blobName);

    @PutMapping("recruitments/candidates/{applicationCode}")
    ResponseEntity<Void> updateCandidate(@PathVariable String applicationCode, @RequestBody DetailedCandidateDTO candidateDto);

    @GetMapping("applications/status/{applicationCode}")
    ResponseEntity<ApplicationStatusDTO> getApplicationStatus(@PathVariable String applicationCode);

}
