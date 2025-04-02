package pl.adamik.library.components.loan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.adamik.library.components.book.Book;
import pl.adamik.library.components.book.BookRepository;
import pl.adamik.library.components.loan.dto.LoanDto;
import pl.adamik.library.components.loan.exeption.InvalidLoadException;
import pl.adamik.library.components.user.User;
import pl.adamik.library.components.user.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    void shouldThrowException_whenBookIsAlreadyLoaned() {
        // Given
        Long bookId = 1L;
        Long userId = 2L;

        LoanDto loanDto = new LoanDto(1L, LocalDate.now(), null, userId, bookId);

        // When
        when(loanRepository.findByBook_IdAndEndDateIsNull(bookId))
                .thenReturn(Optional.of(new Loan()));

        Throwable thrown = catchThrowable(() -> loanService.createLoan(loanDto));

        // Then
        assertThat(thrown)
                .isInstanceOf(InvalidLoadException.class)
                .hasMessageContaining("Ta książka jest aktualnie przez kogoś wypożyczona");

        verify(loanRepository, times(1)).findByBook_IdAndEndDateIsNull(bookId);
    }

    @Test
    void shouldThrowException_whenUserNotFound() {
        // Given
        Long bookId = 1L;
        Long userId = 2L;

        LoanDto loanDto = new LoanDto(1L, LocalDate.now(), null, userId, bookId);

        // When
        when(loanRepository.findByBook_IdAndEndDateIsNull(bookId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> loanService.createLoan(loanDto));

        // Then
        assertThat(thrown)
                .isInstanceOf(InvalidLoadException.class)
                .hasMessageContaining("Brak użytkownika z id: " + userId);

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldThrowException_whenBookNotFound() {
        // Given
        Long bookId = 1L;
        Long userId = 2L;

        LoanDto loanDto = new LoanDto(1L, LocalDate.now(), null, userId, bookId);

        // When
        when(loanRepository.findByBook_IdAndEndDateIsNull(bookId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> loanService.createLoan(loanDto));

        // Then
        assertThat(thrown)
                .isInstanceOf(InvalidLoadException.class)
                .hasMessageContaining("Brak wypożyczenia z id: " + bookId);

        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void shouldCreateLoan_whenUserAndBookExistAndNoActiveLoan() {
        // Given
        Long bookId = 1L;
        Long userId = 2L;

        LoanDto loanDto = new LoanDto(1L, LocalDate.now(), null, userId, bookId);
        User user = new User(userId, "John", "Doe", "12345678901", null);
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Dune");
        book.setAuthor("Frank Herbert");
        book.setIsbn("9780441013593");

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setStartDate(LocalDate.now());

        when(loanRepository.findByBook_IdAndEndDateIsNull(bookId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        // When
        LoanDto result = loanService.createLoan(loanDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.bookId()).isEqualTo(bookId);
        assertThat(result.startDate()).isEqualTo(LocalDate.now());

        verify(loanRepository, times(1)).save(any(Loan.class));
    }
}