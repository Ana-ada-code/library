package pl.adamik.library.components.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.adamik.library.components.book.dto.BookDto;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldReturnAllBooks_whenBooksExist() {
        // Given
        Book book1 = new Book(1L, "Harry Potter", "J.K. Rowling", "9780747532743", null);
        Book book2 = new Book(2L, "Dune", "Frank Herbert", "9780441013593", null);

        BookDto bookDto1 = new BookDto(1L, "Harry Potter", "J.K. Rowling", "9780747532743", null);
        BookDto bookDto2 = new BookDto(2L, "Dune", "Frank Herbert", "9780441013593", null);

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);
        when(bookMapper.toDto(book2)).thenReturn(bookDto2);

        // When
        List<BookDto> result = bookService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(bookDto1);
        assertThat(result.get(1)).isEqualTo(bookDto2);

        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(1)).toDto(book1);
        verify(bookMapper, times(1)).toDto(book2);
    }

    @Test
    void shouldReturnEmptyList_whenNoBooksExist() {
        // Given
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<BookDto> result = bookService.findAll();

        // Then
        assertThat(result).isEmpty();

        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, never()).toDto(any());
    }

    @Test
    void shouldReturnBooks_whenMatchingTitleAuthorOrIsbnExists() {
        // Given
        String searchText = "Harry Potter";

        Book book1 = new Book(1L, "Harry Potter and the Sorcerer’s Stone", "J.K. Rowling", "9780747532743", null);
        Book book2 = new Book(2L, "Fantastic Beasts", "J.K. Rowling", "9781338216790", null);

        BookDto bookDto1 = new BookDto(1L, "Harry Potter and the Sorcerer’s Stone", "J.K. Rowling", "9780747532743", null);
        BookDto bookDto2 = new BookDto(2L, "Fantastic Beasts", "J.K. Rowling", "9781338216790", null);

        when(bookRepository.findAllByTitleOrAuthorOrIsbn(searchText)).thenReturn(List.of(book1, book2));
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);
        when(bookMapper.toDto(book2)).thenReturn(bookDto2);

        // When
        List<BookDto> result = bookService.findByTitleOrAuthorOrIsbn(searchText);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(bookDto1, bookDto2);

        verify(bookRepository, times(1)).findAllByTitleOrAuthorOrIsbn(searchText);
        verify(bookMapper, times(1)).toDto(book1);
        verify(bookMapper, times(1)).toDto(book2);
    }

    @Test
    void shouldReturnEmptyList_whenNoMatchingBooksExist() {
        // Given
        String searchText = "Nonexistent Book";
        when(bookRepository.findAllByTitleOrAuthorOrIsbn(searchText)).thenReturn(Collections.emptyList());

        // When
        List<BookDto> result = bookService.findByTitleOrAuthorOrIsbn(searchText);

        // Then
        assertThat(result).isEmpty();

        verify(bookRepository, times(1)).findAllByTitleOrAuthorOrIsbn(searchText);
        verify(bookMapper, never()).toDto(any());
    }

}