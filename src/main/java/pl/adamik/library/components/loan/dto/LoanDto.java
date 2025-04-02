package pl.adamik.library.components.loan.dto;

import java.time.LocalDate;

public record LoanDto(
        Long id,
        LocalDate start,
        LocalDate finish,
        Long userId,
        Long bookId){
}
