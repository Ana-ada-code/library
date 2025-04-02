package pl.adamik.library.components.loanHistory;

import org.junit.jupiter.api.Test;
import pl.adamik.library.components.book.Book;
import pl.adamik.library.components.loanHistory.dto.LoanHistoryDto;
import pl.adamik.library.components.user.User;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LoanHistoryMapperTest {

    @Test
    void shouldMapLoanHistoryToDtoCorrectly() {
        // Given
        Long loanHistoryId = 1L;
        LocalDate startDate = LocalDate.of(2024, 1, 10);
        LocalDate endDate = LocalDate.of(2024, 1, 20);
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
        LoanHistory loanHistory = new LoanHistory(loanHistoryId, startDate, endDate, user, book);

        // When
        LoanHistoryDto result = LoanHistoryMapper.toDto(loanHistory);

        // Then
        assertThat(result.id()).isEqualTo(loanHistoryId);
        assertThat(result.startDate()).isEqualTo(startDate);
        assertThat(result.endDate()).isEqualTo(endDate);
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.bookId()).isEqualTo(bookId);
    }

    @Test
    void shouldMapLoanHistoryWithNullValues() {
        // Given
        Long loanHistoryId = 2L;
        LocalDate startDate = LocalDate.of(2024, 2, 5);
        LocalDate endDate = null;

        User user = new User(3L, "Alice", "Smith", "98765432109", null);
        Book book = new Book();
        book.setId(4L);
        book.setTitle("1984");
        book.setAuthor("George Orwell");
        book.setIsbn("9780451524935");
        LoanHistory loanHistory = new LoanHistory(loanHistoryId, startDate, endDate, user, book);

        // When
        LoanHistoryDto result = LoanHistoryMapper.toDto(loanHistory);

        // Then
        assertThat(result.id()).isEqualTo(loanHistoryId);
        assertThat(result.startDate()).isEqualTo(startDate);
        assertThat(result.endDate()).isNull();
        assertThat(result.userId()).isEqualTo(user.getId());
        assertThat(result.bookId()).isEqualTo(book.getId());
    }

}