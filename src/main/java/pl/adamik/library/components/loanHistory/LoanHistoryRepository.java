package pl.adamik.library.components.loanHistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanHistoryRepository extends JpaRepository<LoanHistory, Long> {
    Optional<LoanHistory> findByBook_IdAndEndDateIsNull(Long bookId);
}
