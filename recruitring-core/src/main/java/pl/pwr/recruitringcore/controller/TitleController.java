package pl.pwr.recruitringcore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.TitleApi;
import pl.pwr.recruitringcore.dto.TitleDTO;
import pl.pwr.recruitringcore.service.TitleService;

import java.util.List;

@RestController
public class TitleController implements TitleApi {

    private final TitleService titleService;

    public TitleController(TitleService titleService) {
        this.titleService = titleService;
    }

    @Override
    public ResponseEntity<List<TitleDTO>> getTitlesByName(String query) {
        List<TitleDTO> titles = titleService.getTitlesByName(query);
        return ResponseEntity.ok(titles);
    }

    @Override
    public ResponseEntity<TitleDTO> addTitle(String titleName) {
        TitleDTO createdTitle = titleService.addNewTitle(titleName);
        return ResponseEntity.ok(createdTitle);
    }
}
