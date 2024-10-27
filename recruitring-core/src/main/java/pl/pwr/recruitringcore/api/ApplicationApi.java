package pl.pwr.recruitringcore.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pwr.recruitringcore.dto.ApplicationDTO;
import pl.pwr.recruitringcore.dto.CandidateDTO;

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
}
