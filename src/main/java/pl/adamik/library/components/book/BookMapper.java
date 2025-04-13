package pl.adamik.library.components.book;

import org.springframework.stereotype.Service;
import pl.adamik.library.components.book.dto.BookDto;
import pl.adamik.library.components.genre.Genre;
import pl.adamik.library.components.genre.GenreRepository;

import java.util.Optional;

@Service
public class BookMapper {

    private final GenreRepository genreRepository;

    public BookMapper(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                Optional.ofNullable(book.getGenre()).map(Genre::getName).orElse(null)
        );
    }

    Book toEntity(BookDto dto) {
        Genre genreEntity = genreRepository.findByName(dto.getGenre()).orElse(null);

        return Book.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .genre(genreEntity)
                .build();
    }
}
