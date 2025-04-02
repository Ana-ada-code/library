package pl.adamik.library.components.book.dto;

import java.time.LocalDate;

public record BookLoanHistoryDto (
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        Long userId,
        String firstName,
        String lastName,
        String pesel) {
}
