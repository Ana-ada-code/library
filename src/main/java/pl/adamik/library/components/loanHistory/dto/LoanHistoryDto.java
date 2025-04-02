package pl.adamik.library.components.loanHistory.dto;

import java.time.LocalDate;

public record LoanHistoryDto (
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        Long userId,
        Long bookId){
}
