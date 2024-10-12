package pl.pwr.recruitringcore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.pwr.recruitringcore.api.ApplicationApi;
import pl.pwr.recruitringcore.dto.ApplicationDTO;
import pl.pwr.recruitringcore.service.ApplicationService;

@RestController
public class ApplicationController implements ApplicationApi {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public ResponseEntity<String> createApplication(ApplicationDTO applicationDto, MultipartFile cvFile) {
        return applicationService.processApplication(applicationDto, cvFile);
    }
}
