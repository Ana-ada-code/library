package pl.adamik.library.components.loan;

import pl.adamik.library.components.loan.dto.LoanDto;

public class LoanMapper {

    static LoanDto toDto(Loan loan) {
        return new LoanDto(
                loan.getId(),
                loan.getStartDate(),
                loan.getEndDate(),
                loan.getUser().getId(),
                loan.getBook().getId());
    }
}
