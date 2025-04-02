package pl.adamik.library.components.user.dto;

import java.time.LocalDate;

public record UserLoanHistoryDto(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        Long bookId,
        String bookTitle,
        String bookAuthor,
        String bookIsbn) {
}
