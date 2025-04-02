package pl.adamik.library.components.loan.exeption;

public class InvalidLoadException extends RuntimeException {
    public InvalidLoadException(String message) {
        super(message);
    }
}
