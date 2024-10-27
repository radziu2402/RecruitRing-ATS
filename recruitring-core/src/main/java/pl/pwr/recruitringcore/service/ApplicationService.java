package pl.pwr.recruitringcore.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.pwr.recruitringcore.dto.ApplicationDTO;
import pl.pwr.recruitringcore.dto.CandidateDTO;

import java.util.List;

public interface ApplicationService {

    ResponseEntity<String> processApplication(ApplicationDTO applicationDto, MultipartFile cvFile);

    List<CandidateDTO> getCandidatesByOfferCode(String offerCode);
}
