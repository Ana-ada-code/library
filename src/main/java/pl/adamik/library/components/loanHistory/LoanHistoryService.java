package pl.adamik.library.components.loanHistory;

import org.springframework.stereotype.Service;
import pl.adamik.library.components.book.Book;
import pl.adamik.library.components.book.BookRepository;
import pl.adamik.library.components.loanHistory.dto.LoanHistoryDto;
import pl.adamik.library.components.loanHistory.exeption.InvalidLoadHistoryException;
import pl.adamik.library.components.user.User;
import pl.adamik.library.components.user.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class LoanHistoryService {

    private final LoanHistoryRepository loanHistoryRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoanHistoryService(LoanHistoryRepository loanHistoryRepository,
                              BookRepository bookRepository,
                              UserRepository userRepository) {
        this.loanHistoryRepository = loanHistoryRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    LoanHistoryDto createLoanHistory(LoanHistoryDto loanHistoryDto) {
        Optional<LoanHistory> activeLoanForBook = loanHistoryRepository.findByBook_IdAndEndDateIsNull(loanHistoryDto.id());

        activeLoanForBook.ifPresent((a) -> {
            throw new InvalidLoadHistoryException("Ta książka jest aktualnie przez kogoś wypożyczona");
        });

        Optional<User> user = userRepository.findById(loanHistoryDto.userId());
        Optional<Book> book = bookRepository.findById(loanHistoryDto.bookId());
        LoanHistory loanHistory = new LoanHistory();
        Long userId = loanHistoryDto.userId();
        Long bookId = loanHistoryDto.bookId();
        loanHistory.setUser(user.orElseThrow(() ->
        new InvalidLoadHistoryException("Brak użytkownika z id: " + userId)));
        loanHistory.setBook(book.orElseThrow(() ->
                new InvalidLoadHistoryException("Brak wypożyczenia z id: " + bookId)));
        loanHistory.setStartDate(LocalDate.now());
        return LoanHistoryMapper.toDto(loanHistoryRepository.save(loanHistory));
    }
}
