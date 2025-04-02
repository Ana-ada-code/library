package pl.adamik.library.components.loan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByBook_IdAndFinishIsNull(Long bookId);
}
