package pl.adamik.library.components.loan;

import org.springframework.stereotype.Service;
import pl.adamik.library.components.book.Book;
import pl.adamik.library.components.book.BookRepository;
import pl.adamik.library.components.loan.dto.LoanDto;
import pl.adamik.library.components.loan.exeption.InvalidLoadException;
import pl.adamik.library.components.user.User;
import pl.adamik.library.components.user.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoanService(LoanRepository loanRepository,
                       BookRepository bookRepository,
                       UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    LoanDto createLoan(LoanDto loanDto) {
        Optional<Loan> activeLoanForBook = loanRepository.findByBook_IdAndFinishIsNull(loanDto.id());

        activeLoanForBook.ifPresent((a) -> {
            throw new InvalidLoadException("Ta książka jest aktualnie przez kogoś wypożyczona");
        });

        Optional<User> user = userRepository.findById(loanDto.userId());
        Optional<Book> book = bookRepository.findById(loanDto.bookId());
        Loan loan = new Loan();
        Long userId = loanDto.userId();
        Long bookId = loanDto.bookId();
        loan.setUser(user.orElseThrow(() ->
        new InvalidLoadException("Brak użytkownika z id: " + userId)));
        loan.setBook(book.orElseThrow(() ->
                new InvalidLoadException("Brak wypożyczenia z id: " + bookId)));
        loan.setStart(LocalDate.now());
        return LoanMapper.toDto(loanRepository.save(loan));
    }
}
