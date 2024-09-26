package pl.pwr.recruitringcore.service;

import org.springframework.stereotype.Service;
import pl.pwr.recruitringcore.dto.TitleDTO;
import pl.pwr.recruitringcore.model.entities.Title;
import pl.pwr.recruitringcore.repo.TitleRepository;

import java.util.List;

@Service
public class TitleServiceImpl implements TitleService {

    private final TitleRepository titleRepository;

    public TitleServiceImpl(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    @Override
    public List<TitleDTO> getTitlesByName(String query) {
        return titleRepository.findByNameContainingIgnoreCase(query).stream()
                .map(this::mapToDTO)
                .toList();
    }

    private TitleDTO mapToDTO(Title title) {
        return TitleDTO.builder()
                .id(title.getId())
                .name(title.getName())
                .build();
    }
}
