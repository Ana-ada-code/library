package pl.adamik.library.components.loan;

import org.junit.jupiter.api.Test;
import pl.adamik.library.components.book.Book;
import pl.adamik.library.components.loan.dto.LoanDto;
import pl.adamik.library.components.user.User;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LoanMapperTest {

    @Test
    void shouldMapLoanToDtoCorrectly() {
        // Given
        Long loanId = 1L;
        LocalDate start = LocalDate.of(2024, 1, 10);
        LocalDate finish = LocalDate.of(2024, 1, 20);
        Long userId = 2L;
        Long bookId = 3L;

        User user = new User();
        user.setId(userId);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPesel("12345678901");
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Dune");
        book.setAuthor("Frank Herbert");
        book.setIsbn("9780441013593");
        Loan loan = new Loan(loanId, start, finish, user, book);

        // When
        LoanDto result = LoanMapper.toDto(loan);

        // Then
        assertThat(result.id()).isEqualTo(loanId);
        assertThat(result.start()).isEqualTo(start);
        assertThat(result.finish()).isEqualTo(finish);
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.bookId()).isEqualTo(bookId);
    }

    @Test
    void shouldMapLoanWithNullValues() {
        // Given
        Long loanId = 2L;
        LocalDate start = LocalDate.of(2024, 2, 5);
        LocalDate finish = null;

        User user = new User(3L, "Alice", "Smith", "98765432109", null);
        Book book = new Book();
        book.setId(4L);
        book.setTitle("1984");
        book.setAuthor("George Orwell");
        book.setIsbn("9780451524935");
        Loan loan = new Loan(loanId, start, finish, user, book);

        // When
        LoanDto result = LoanMapper.toDto(loan);

        // Then
        assertThat(result.id()).isEqualTo(loanId);
        assertThat(result.start()).isEqualTo(start);
        assertThat(result.finish()).isNull();
        assertThat(result.userId()).isEqualTo(user.getId());
        assertThat(result.bookId()).isEqualTo(book.getId());
    }

}