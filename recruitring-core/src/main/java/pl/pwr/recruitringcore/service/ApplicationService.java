package pl.pwr.recruitringcore.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.pwr.recruitringcore.dto.ApplicationDTO;
import pl.pwr.recruitringcore.dto.ApplicationStatusDTO;
import pl.pwr.recruitringcore.dto.CandidateDTO;
import pl.pwr.recruitringcore.dto.DetailedCandidateDTO;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {

    ResponseEntity<String> processApplication(ApplicationDTO applicationDto, MultipartFile cvFile);

    List<CandidateDTO> getCandidatesByOfferCode(String offerCode);

    DetailedCandidateDTO getCandidateDetails(UUID applicationCode);

    boolean updateCandidate(UUID applicationCode, DetailedCandidateDTO candidateDto);

    ApplicationStatusDTO getApplicationStatus(String applicationCode);

}
