package pl.pwr.recruitringcore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.pwr.recruitringcore.api.ApplicationApi;
import pl.pwr.recruitringcore.blobstorage.AzureBlobStorageService;
import pl.pwr.recruitringcore.dto.ApplicationDTO;
import pl.pwr.recruitringcore.dto.CandidateDTO;
import pl.pwr.recruitringcore.dto.DetailedCandidateDTO;
import pl.pwr.recruitringcore.service.ApplicationService;

import java.util.List;
import java.util.UUID;

@RestController
public class ApplicationController implements ApplicationApi {

    private final ApplicationService applicationService;
    private final AzureBlobStorageService azureBlobStorageService;


    public ApplicationController(ApplicationService applicationService, AzureBlobStorageService azureBlobStorageService) {
        this.applicationService = applicationService;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    @Override
    public ResponseEntity<String> createApplication(ApplicationDTO applicationDto, MultipartFile cvFile) {
        return applicationService.processApplication(applicationDto, cvFile);
    }

    @Override
    public ResponseEntity<List<CandidateDTO>> getCandidatesByOfferCode(String offerCode) {
        List<CandidateDTO> candidates = applicationService.getCandidatesByOfferCode(offerCode);
        return ResponseEntity.ok(candidates);
    }

    @Override
    public ResponseEntity<DetailedCandidateDTO> getCandidateDetails(String applicationCode) {
        DetailedCandidateDTO candidateDetails = applicationService.getCandidateDetails(UUID.fromString(applicationCode));
        return candidateDetails != null ? ResponseEntity.ok(candidateDetails) : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> getFileUrlToDownload(String blobName) {
        return azureBlobStorageService.getFileUrlToDownload(blobName);
    }

    @Override
    public ResponseEntity<Void> updateCandidate(String applicationCode, DetailedCandidateDTO candidateDto) {
        boolean updated = applicationService.updateCandidate(UUID.fromString(applicationCode), candidateDto);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
