package pl.adamik.library.components.book;

import pl.adamik.library.components.book.dto.BookLoanDto;
import pl.adamik.library.components.loan.Loan;

class BookLoanMapper {

    static BookLoanDto toDto(Loan loan) {
        return new BookLoanDto(
                loan.getId(),
                loan.getStart(),
                loan.getFinish(),
                loan.getUser().getId(),
                loan.getUser().getFirstName(),
                loan.getUser().getLastName(),
                loan.getUser().getPesel());
    }
}
