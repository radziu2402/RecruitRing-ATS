package pl.pwr.recruitringcore.service;

import pl.pwr.recruitringcore.dto.TitleDTO;

import java.util.List;

public interface TitleService {
    List<TitleDTO> getTitlesByName(String query);
    TitleDTO addNewTitle(String titleName);
}
