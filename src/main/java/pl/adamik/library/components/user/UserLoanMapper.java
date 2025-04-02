package pl.adamik.library.components.user;

import pl.adamik.library.components.loan.Loan;
import pl.adamik.library.components.user.dto.UserLoanDto;

public class UserLoanMapper {

    static UserLoanDto toDto(Loan loan) {
        return new UserLoanDto(
                loan.getId(),
                loan.getStart(),
                loan.getFinish(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getBook().getAuthor(),
                loan.getBook().getIsbn()
        );
    }

}
