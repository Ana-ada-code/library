package pl.adamik.library.components.user.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "There is no user with this ID")
public class UserNotFoundException extends RuntimeException {}
