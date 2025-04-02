package pl.adamik.library.components.book;

import pl.adamik.library.components.book.dto.BookLoanDto;
import pl.adamik.library.components.loan.Loan;

public class BookLoanMapper {

    static BookLoanDto toDto(Loan loan) {
        return new BookLoanDto(
                loan.getId(),
                loan.getStartDate(),
                loan.getEndDate(),
                loan.getUser().getId(),
                loan.getUser().getFirstName(),
                loan.getUser().getLastName(),
                loan.getUser().getPesel());
    }
}
