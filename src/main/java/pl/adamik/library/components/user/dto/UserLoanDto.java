package pl.adamik.library.components.user.dto;

import java.time.LocalDate;

public record UserLoanDto(
        Long id,
        LocalDate start,
        LocalDate finish,
        Long bookId,
        String bookTitle,
        String bookAuthor,
        String bookIsbn) {
}
