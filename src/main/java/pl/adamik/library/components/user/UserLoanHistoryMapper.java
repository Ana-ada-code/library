package pl.adamik.library.components.user;

import pl.adamik.library.components.loanHistory.LoanHistory;
import pl.adamik.library.components.user.dto.UserLoanHistoryDto;

public class UserLoanHistoryMapper {

    static UserLoanHistoryDto toDto(LoanHistory loanHistory) {
        return new UserLoanHistoryDto(
                loanHistory.getId(),
                loanHistory.getStartDate(),
                loanHistory.getEndDate(),
                loanHistory.getBook().getId(),
                loanHistory.getBook().getTitle(),
                loanHistory.getBook().getAuthor(),
                loanHistory.getBook().getIsbn()
        );
    }

}
