package pl.adamik.library.components.loan.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Nie istnieje wypożyczenie o takim Id")
public class LoanNotFoundException extends RuntimeException {
}