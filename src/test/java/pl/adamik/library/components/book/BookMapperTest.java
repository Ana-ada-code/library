package pl.adamik.library.components.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.adamik.library.components.book.dto.BookDto;
import pl.adamik.library.components.genre.Genre;
import pl.adamik.library.components.genre.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookMapperTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private BookMapper bookMapper;

    @Test
    void shouldMapBookToDto_whenGenreIsNotNull() {
        // Given
        Genre genre = new Genre();
        genre.setName("Fantasy");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Harry Potter");
        book.setAuthor("J.K. Rowling");
        book.setIsbn("9780747532743");
        book.setGenre(genre);

        // When
        BookDto result = bookMapper.toDto(book);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Harry Potter");
        assertThat(result.getAuthor()).isEqualTo("J.K. Rowling");
        assertThat(result.getIsbn()).isEqualTo("9780747532743");
        assertThat(result.getGenre()).isEqualTo("Fantasy");
    }

    @Test
    void shouldMapBookToDto_whenGenreIsNull() {
        // Given
        Book book = new Book();
        book.setId(2L);
        book.setTitle("1984");
        book.setAuthor("George Orwell");
        book.setIsbn("9780451524935");
        book.setGenre(null);

        // When
        BookDto result = bookMapper.toDto(book);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getTitle()).isEqualTo("1984");
        assertThat(result.getAuthor()).isEqualTo("George Orwell");
        assertThat(result.getIsbn()).isEqualTo("9780451524935");
        assertThat(result.getGenre()).isNull();
    }

    @Test
    void shouldMapDtoToEntity_whenGenreExists() {
        // Given
        String genreName = "Fantasy";
        Genre genre = new Genre();
        genre.setName(genreName);

        BookDto bookDto = new BookDto(1L, "Harry Potter", "J.K. Rowling", "9780747532743", genreName);

        when(genreRepository.findByName(genreName)).thenReturn(Optional.of(genre));

        // When
        Book result = bookMapper.toEntity(bookDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Harry Potter");
        assertThat(result.getAuthor()).isEqualTo("J.K. Rowling");
        assertThat(result.getIsbn()).isEqualTo("9780747532743");
        assertThat(result.getGenre()).isEqualTo(genre);

        verify(genreRepository, times(1)).findByName(genreName);
    }

    @Test
    void shouldMapDtoToEntity_whenGenreDoesNotExist() {
        // Given
        String genreName = "Unknown Genre";

        BookDto bookDto = new BookDto(2L, "Dune", "Frank Herbert", "9780441013593", genreName);

        when(genreRepository.findByName(genreName)).thenReturn(Optional.empty());

        // When
        Book result = bookMapper.toEntity(bookDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getTitle()).isEqualTo("Dune");
        assertThat(result.getAuthor()).isEqualTo("Frank Herbert");
        assertThat(result.getIsbn()).isEqualTo("9780441013593");
        assertThat(result.getGenre()).isNull();

        verify(genreRepository, times(1)).findByName(genreName);
    }
}
