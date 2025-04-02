package pl.adamik.library.components.loanHistory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.adamik.library.components.loanHistory.dto.LoanHistoryDto;
import pl.adamik.library.components.loanHistory.exeption.InvalidLoadHistoryException;

import java.net.URI;

@RestController
@RequestMapping("/api/loan-histories")
public class LoanHistoryResource {

    private final LoanHistoryService loanHistoryService;

    public LoanHistoryResource(LoanHistoryService loanHistoryService) {
        this.loanHistoryService = loanHistoryService;
    }

    @PostMapping("")
    public ResponseEntity<LoanHistoryDto> saveLoanHistory(@RequestBody LoanHistoryDto loanHistory) {
        LoanHistoryDto savedLoanHistory;
        try {
            savedLoanHistory = loanHistoryService.createLoanHistory(loanHistory);
        } catch(InvalidLoadHistoryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedLoanHistory.id())
                .toUri();
        return ResponseEntity.created(location).body(savedLoanHistory);
    }
}
