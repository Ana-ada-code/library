package pl.adamik.library.components.loanHistory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.adamik.library.components.book.Book;
import pl.adamik.library.components.book.BookRepository;
import pl.adamik.library.components.loanHistory.dto.LoanHistoryDto;
import pl.adamik.library.components.loanHistory.exeption.InvalidLoadHistoryException;
import pl.adamik.library.components.user.User;
import pl.adamik.library.components.user.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanHistoryServiceTest {

    @Mock
    private LoanHistoryRepository loanHistoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LoanHistoryService loanHistoryService;

    @Test
    void shouldThrowException_whenBookIsAlreadyLoaned() {
        // Given
        Long bookId = 1L;
        Long userId = 2L;

        LoanHistoryDto loanHistoryDto = new LoanHistoryDto(1L, LocalDate.now(), null, userId, bookId);

        // When
        when(loanHistoryRepository.findByBook_IdAndEndDateIsNull(bookId))
                .thenReturn(Optional.of(new LoanHistory())); // Book is already loaned

        Throwable thrown = catchThrowable(() -> loanHistoryService.createLoanHistory(loanHistoryDto));

        // Then
        assertThat(thrown)
                .isInstanceOf(InvalidLoadHistoryException.class)
                .hasMessageContaining("Ta książka jest aktualnie przez kogoś wypożyczona");

        verify(loanHistoryRepository, times(1)).findByBook_IdAndEndDateIsNull(bookId);
    }

    @Test
    void shouldThrowException_whenUserNotFound() {
        // Given
        Long bookId = 1L;
        Long userId = 2L;

        LoanHistoryDto loanHistoryDto = new LoanHistoryDto(1L, LocalDate.now(), null, userId, bookId);

        // When
        when(loanHistoryRepository.findByBook_IdAndEndDateIsNull(bookId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty()); // User does not exist

        Throwable thrown = catchThrowable(() -> loanHistoryService.createLoanHistory(loanHistoryDto));

        // Then
        assertThat(thrown)
                .isInstanceOf(InvalidLoadHistoryException.class)
                .hasMessageContaining("Brak użytkownika z id: " + userId);

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldThrowException_whenBookNotFound() {
        // Given
        Long bookId = 1L;
        Long userId = 2L;

        LoanHistoryDto loanHistoryDto = new LoanHistoryDto(1L, LocalDate.now(), null, userId, bookId);

        // When
        when(loanHistoryRepository.findByBook_IdAndEndDateIsNull(bookId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User())); // User exists
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty()); // Book does not exist

        Throwable thrown = catchThrowable(() -> loanHistoryService.createLoanHistory(loanHistoryDto));

        // Then
        assertThat(thrown)
                .isInstanceOf(InvalidLoadHistoryException.class)
                .hasMessageContaining("Brak wypożyczenia z id: " + bookId);

        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void shouldCreateLoanHistory_whenUserAndBookExistAndNoActiveLoan() {
        // Given
        Long bookId = 1L;
        Long userId = 2L;

        LoanHistoryDto loanHistoryDto = new LoanHistoryDto(1L, LocalDate.now(), null, userId, bookId);
        User user = new User(userId, "John", "Doe", "12345678901", null);
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Dune");
        book.setAuthor("Frank Herbert");
        book.setIsbn("9780441013593");

        LoanHistory loanHistory = new LoanHistory();
        loanHistory.setUser(user);
        loanHistory.setBook(book);
        loanHistory.setStartDate(LocalDate.now());

        when(loanHistoryRepository.findByBook_IdAndEndDateIsNull(bookId)).thenReturn(Optional.empty()); // No active loan
        when(userRepository.findById(userId)).thenReturn(Optional.of(user)); // User exists
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book)); // Book exists
        when(loanHistoryRepository.save(any(LoanHistory.class))).thenReturn(loanHistory); // Saving loan history

        // When
        LoanHistoryDto result = loanHistoryService.createLoanHistory(loanHistoryDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.bookId()).isEqualTo(bookId);
        assertThat(result.startDate()).isEqualTo(LocalDate.now());

        verify(loanHistoryRepository, times(1)).save(any(LoanHistory.class));
    }
}