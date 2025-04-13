package pl.adamik.library.components.loan.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "This loan has already been completed")
public class LoanAlreadyFinishedException extends RuntimeException {
}
