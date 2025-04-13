package pl.adamik.library.components.loan;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.adamik.library.components.book.Book;
import pl.adamik.library.components.book.BookRepository;
import pl.adamik.library.components.loan.dto.LoanDto;
import pl.adamik.library.components.loan.exeption.InvalidLoadException;
import pl.adamik.library.components.loan.exeption.LoanAlreadyFinishedException;
import pl.adamik.library.components.loan.exeption.LoanNotFoundException;
import pl.adamik.library.components.user.User;
import pl.adamik.library.components.user.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
class LoanService {

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
            throw new InvalidLoadException("This book is currently being borrowed by someone");
        });

        Optional<User> user = userRepository.findById(loanDto.userId());
        Optional<Book> book = bookRepository.findById(loanDto.bookId());
        Loan loan = new Loan();
        Long userId = loanDto.userId();
        Long bookId = loanDto.bookId();
        loan.setUser(user.orElseThrow(() ->
                new InvalidLoadException("No user with ID: " + userId)));
        loan.setBook(book.orElseThrow(() ->
                new InvalidLoadException("No user with ID: " + bookId)));
        loan.setStart(LocalDate.now());
        return LoanMapper.toDto(loanRepository.save(loan));
    }

    @Transactional
    public LocalDate finishLoan(Long loanId) {
        Optional<Loan> loan = loanRepository.findById(loanId);
        Loan loanEntity = loan.orElseThrow(LoanNotFoundException::new);
        if (loanEntity.getFinish() != null) {
            throw new LoanAlreadyFinishedException();
        } else {
            loanEntity.setFinish(LocalDate.now());
        }
        return loanEntity.getFinish();
    }
}
