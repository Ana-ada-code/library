package pl.adamik.library.components.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.adamik.library.components.book.dto.BookDto;
import pl.adamik.library.components.book.dto.BookLoanDto;
import pl.adamik.library.components.book.exeption.BookNotFoundException;
import pl.adamik.library.components.loan.Loan;
import pl.adamik.library.components.user.User;
import pl.adamik.library.components.user.UserRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldReturnAllBooks_whenBooksExist() {
        // Given
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Harry Potter");
        book1.setAuthor("J.K. Rowling");
        book1.setIsbn("9780747532743");
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Frank Herbert");
        book2.setIsbn("9780441013593");

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

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Harry Potter and the Sorcerer’s Stone");
        book1.setAuthor("J.K. Rowling");
        book1.setIsbn("9780747532743");
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("J.K. Rowling");
        book2.setIsbn("9781338216790");

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

    @Test
    void shouldSaveBookSuccessfully() {
        // Given
        BookDto bookDto = new BookDto(null, "Testowy Tytuł", "Testowy Autor", "123456789", "Fantasy");
        Book bookEntity = new Book();
        bookEntity.setTitle("Testowy Tytuł");
        bookEntity.setAuthor("Testowy Autor");
        bookEntity.setIsbn("123456789");
        Book savedBookEntity = new Book();
        savedBookEntity.setId(1L);
        bookEntity.setTitle("Testowy Tytuł");
        bookEntity.setAuthor("Testowy Autor");
        savedBookEntity.setIsbn("123456789");
        BookDto expectedDto = new BookDto(1L, "Testowy Tytuł", "Testowy Autor", "123456789", "Fantasy");

        when(bookMapper.toEntity(bookDto)).thenReturn(bookEntity);
        when(bookRepository.save(bookEntity)).thenReturn(savedBookEntity);
        when(bookMapper.toDto(savedBookEntity)).thenReturn(expectedDto);

        // When
        BookDto result = bookService.save(bookDto);

        // Then
        assertThat(result).isEqualTo(expectedDto);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Testowy Tytuł");
        assertThat(result.getAuthor()).isEqualTo("Testowy Autor");
        assertThat(result.getIsbn()).isEqualTo("123456789");
        assertThat(result.getGenre()).isEqualTo("Fantasy");

        verify(bookMapper, times(1)).toEntity(bookDto);
        verify(bookRepository, times(1)).save(bookEntity);
        verify(bookMapper, times(1)).toDto(savedBookEntity);
    }

    @Test
    void shouldThrowException_whenSavingFails() {
        // Given
        BookDto bookDto = new BookDto(null, "Testowy Tytuł", "Testowy Autor", "123456789", "Fantasy");
        Book bookEntity = new Book();
        bookEntity.setTitle("Testowy Tytuł");
        bookEntity.setAuthor("Testowy Autor");
        bookEntity.setIsbn("123456789");

        when(bookMapper.toEntity(bookDto)).thenReturn(bookEntity);
        when(bookRepository.save(bookEntity)).thenThrow(new RuntimeException("Błąd zapisu w bazie"));

        // When / Then
        assertThatThrownBy(() -> bookService.save(bookDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Błąd zapisu w bazie");

        verify(bookMapper, times(1)).toEntity(bookDto);
        verify(bookRepository, times(1)).save(bookEntity);
        verify(bookMapper, never()).toDto(any());
    }

    @Test
    void shouldReturnBookDto_whenBookExists() {
        // Given
        Long bookId = 1L;
        Book bookEntity = new Book();
        bookEntity.setId(bookId);
        bookEntity.setTitle("Harry Potter");
        bookEntity.setAuthor("J.K. Rowling");
        bookEntity.setIsbn("9780747532743");
        BookDto expectedDto = new BookDto(bookId, "Harry Potter", "J.K. Rowling", "9780747532743", "Fantasy");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        when(bookMapper.toDto(bookEntity)).thenReturn(expectedDto);

        // When
        Optional<BookDto> result = bookService.findById(bookId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedDto);
        assertThat(result.get().getId()).isEqualTo(bookId);
        assertThat(result.get().getTitle()).isEqualTo("Harry Potter");

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).toDto(bookEntity);
    }

    @Test
    void shouldReturnEmptyOptional_whenBookDoesNotExist() {
        // Given
        Long bookId = 99L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When
        Optional<BookDto> result = bookService.findById(bookId);

        // Then
        assertThat(result).isEmpty();

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, never()).toDto(any());
    }

    @Test
    void shouldReturnBookLoan_whenBookExists() {
        // Given
        Long bookId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPesel("12345678901");

        Loan loan1 = new Loan(10L, LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 20), user, null);
        Loan loan2 = new Loan(11L, LocalDate.of(2024, 2, 5), null, user, null);

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Dune");
        book.setAuthor("Frank Herbert");
        book.setIsbn("9780441013593");
        book.setLoans(List.of(loan1, loan2));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        List<BookLoanDto> expectedDtos = book.getLoans().stream()
                .map(BookLoanMapper::toDto)
                .collect(Collectors.toList());

        // When
        List<BookLoanDto> result = bookService.findBookLoans(bookId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(expectedDtos);

        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void shouldThrowException_whenBookNotFound() {
        // Given
        Long bookId = 99L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookService.findBookLoans(bookId))
                .isInstanceOf(BookNotFoundException.class);

        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void shouldReturnEmptyList_whenBookHasNoLoanHistories() {
        // Given
        Long bookId = 2L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("1984");
        book.setAuthor("George Orwell");
        book.setIsbn("9780451524935");
        book.setLoans(List.of());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // When
        List<BookLoanDto> result = bookService.findBookLoans(bookId);

        // Then
        assertThat(result).isEmpty();

        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void deleteBook_existingBook_shouldDeleteAndReturnTrue() {
        // given
        Long bookId = 1L;
        doNothing().when(bookRepository).deleteById(bookId);

        // when
        boolean result = bookService.deleteBook(bookId);

        // then
        assertTrue(result);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void deleteBook_nonExistingBook_shouldThrowException() {
        // given
        Long bookId = 999L;
        doThrow(new RuntimeException("Book not found")).when(bookRepository).deleteById(bookId);

        // when / then
        assertThrows(RuntimeException.class, () -> bookService.deleteBook(bookId));
        verify(bookRepository, times(1)).deleteById(bookId);
    }

}
