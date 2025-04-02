package pl.adamik.library.components.user;

import org.junit.jupiter.api.Test;
import pl.adamik.library.components.book.Book;
import pl.adamik.library.components.loan.Loan;
import pl.adamik.library.components.user.dto.UserLoanDto;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserLoanMapperTest {

    @Test
    void shouldMapLoanToDto() {
        // Given
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Dune");
        book.setAuthor("Frank Herbert");
        book.setIsbn("9780441013593");
        Loan loan = new Loan(10L, LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 20), null, book);

        // When
        UserLoanDto dto = UserLoanMapper.toDto(loan);

        // Then
        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.startDate()).isEqualTo(LocalDate.of(2024, 1, 10));
        assertThat(dto.endDate()).isEqualTo(LocalDate.of(2024, 1, 20));
        assertThat(dto.bookId()).isEqualTo(1L);
        assertThat(dto.bookTitle()).isEqualTo("Dune");
        assertThat(dto.bookAuthor()).isEqualTo("Frank Herbert");
        assertThat(dto.bookIsbn()).isEqualTo("9780441013593");
    }

    @Test
    void shouldHandleNullEndDate() {
        // Given
        Book book = new Book();
        book.setId(2L);
        book.setTitle("1984");
        book.setAuthor("George Orwell");
        book.setIsbn("9780451524935");
        Loan loan = new Loan(11L, LocalDate.of(2024, 2, 5), null, null, book);

        // When
        UserLoanDto dto = UserLoanMapper.toDto(loan);

        // Then
        assertThat(dto.id()).isEqualTo(11L);
        assertThat(dto.startDate()).isEqualTo(LocalDate.of(2024, 2, 5));
        assertThat(dto.endDate()).isNull();
        assertThat(dto.bookId()).isEqualTo(2L);
        assertThat(dto.bookTitle()).isEqualTo("1984");
        assertThat(dto.bookAuthor()).isEqualTo("George Orwell");
        assertThat(dto.bookIsbn()).isEqualTo("9780451524935");
    }
}