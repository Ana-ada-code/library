package pl.adamik.library.components.loan;

import pl.adamik.library.components.loan.dto.LoanDto;

public class LoanMapper {

    static LoanDto toDto(Loan loan) {
        return new LoanDto(
                loan.getId(),
                loan.getStart(),
                loan.getFinish(),
                loan.getUser().getId(),
                loan.getBook().getId());
    }
}
