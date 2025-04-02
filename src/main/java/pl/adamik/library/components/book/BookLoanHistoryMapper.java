package pl.adamik.library.components.book;

import pl.adamik.library.components.book.dto.BookLoanHistoryDto;
import pl.adamik.library.components.loanHistory.LoanHistory;

public class BookLoanHistoryMapper {

    static BookLoanHistoryDto toDto(LoanHistory loanHistory) {
        return new BookLoanHistoryDto(
                loanHistory.getId(),
                loanHistory.getStartDate(),
                loanHistory.getEndDate(),
                loanHistory.getUser().getId(),
                loanHistory.getUser().getFirstName(),
                loanHistory.getUser().getLastName(),
                loanHistory.getUser().getPesel());
    }
}
