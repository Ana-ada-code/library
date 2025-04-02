package pl.adamik.library.components.loan.dto;

import java.time.LocalDate;

public record LoanDto(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        Long userId,
        Long bookId){
}
