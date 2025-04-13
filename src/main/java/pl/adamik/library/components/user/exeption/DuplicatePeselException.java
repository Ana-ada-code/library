package pl.adamik.library.components.user.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "A user with this PESEL number already exists")
public class DuplicatePeselException extends RuntimeException { }
