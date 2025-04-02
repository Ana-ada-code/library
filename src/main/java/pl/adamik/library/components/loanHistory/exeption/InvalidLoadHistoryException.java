package pl.adamik.library.components.loanHistory.exeption;

public class InvalidLoadHistoryException extends RuntimeException {
    public InvalidLoadHistoryException(String message) {
        super(message);
    }
}
