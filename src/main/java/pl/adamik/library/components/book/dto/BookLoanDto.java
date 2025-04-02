package pl.adamik.library.components.book.dto;

import java.time.LocalDate;

public record BookLoanDto(
        Long id,
        LocalDate start,
        LocalDate finish,
        Long userId,
        String firstName,
        String lastName,
        String pesel) {
}
