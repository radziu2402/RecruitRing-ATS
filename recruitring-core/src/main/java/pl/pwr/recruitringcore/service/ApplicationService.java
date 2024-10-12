package pl.pwr.recruitringcore.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.pwr.recruitringcore.dto.ApplicationDTO;

public interface ApplicationService {

    ResponseEntity<String> processApplication(ApplicationDTO applicationDto, MultipartFile cvFile);
}
