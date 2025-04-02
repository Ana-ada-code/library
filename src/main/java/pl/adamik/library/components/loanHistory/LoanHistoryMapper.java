package pl.adamik.library.components.loanHistory;

import pl.adamik.library.components.loanHistory.dto.LoanHistoryDto;

public class LoanHistoryMapper {

    static LoanHistoryDto toDto(LoanHistory loanHistory) {
        return new LoanHistoryDto(
                loanHistory.getId(),
                loanHistory.getStartDate(),
                loanHistory.getEndDate(),
                loanHistory.getUser().getId(),
                loanHistory.getBook().getId());
    }
}
