package pl.adamik.library.components.genre;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    List<String> findAllNames() {
        return genreRepository.findAll()
                .stream()
                .map(Genre::getName)
                .collect(Collectors.toList());
    }
}
