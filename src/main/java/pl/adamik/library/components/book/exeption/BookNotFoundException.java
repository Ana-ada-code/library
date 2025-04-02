package pl.adamik.library.components.book.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Brak książki o takim Id")
public class BookNotFoundException extends RuntimeException {
}
