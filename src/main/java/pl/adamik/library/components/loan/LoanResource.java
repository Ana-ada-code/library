package pl.adamik.library.components.loan;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.adamik.library.components.loan.dto.LoanDto;
import pl.adamik.library.components.loan.exeption.InvalidLoadException;

import java.net.URI;

@RestController
@RequestMapping("/api/loans")
public class LoanResource {

    private final LoanService loanService;

    public LoanResource(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("")
    public ResponseEntity<LoanDto> saveLoan(@RequestBody LoanDto loan) {
        LoanDto savedLoan;
        try {
            savedLoan = loanService.createLoan(loan);
        } catch(InvalidLoadException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedLoan.id())
                .toUri();
        return ResponseEntity.created(location).body(savedLoan);
    }
}
