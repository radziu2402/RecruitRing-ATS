package pl.pwr.recruitringcore.service;

import pl.pwr.recruitringcore.dto.RecruiterDTO;

import java.util.List;

public interface RecruiterService {

    List<RecruiterDTO> findRecruitersByName(String query);

}
