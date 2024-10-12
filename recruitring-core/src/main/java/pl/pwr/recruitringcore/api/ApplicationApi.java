package pl.pwr.recruitringcore.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pwr.recruitringcore.dto.ApplicationDTO;

@RequestMapping("api/v1/")
public interface ApplicationApi {

    @PostMapping(value = "apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> createApplication(
            @RequestPart("application") ApplicationDTO applicationDto,
            @RequestPart("cv") MultipartFile cvFile
    );
}
