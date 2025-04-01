package pl.adamik.library.components.genre;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    @Test
    void shouldReturnAllGenreNames_whenGenresExist() {
        // Given
        Genre genre1 = Genre.builder().id(1L).name("Fantasy").description("Magiczne światy i przygody").build();
        Genre genre2 = Genre.builder().id(2L).name("Science Fiction").description("Technologia i przyszłość").build();
        Genre genre3 = Genre.builder().id(3L).name("Horror").description("Strach i tajemnica").build();

        when(genreRepository.findAll()).thenReturn(List.of(genre1, genre2, genre3));

        // When
        List<String> result = genreService.findAllNames();

        // Then
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly("Fantasy", "Science Fiction", "Horror");

        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyList_whenNoGenresExist() {
        // Given
        when(genreRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<String> result = genreService.findAllNames();

        // Then
        assertThat(result).isEmpty();

        verify(genreRepository, times(1)).findAll();
    }
}