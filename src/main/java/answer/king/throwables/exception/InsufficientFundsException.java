package answer.king.throwables.exception;

public class InsufficientFundsException extends Exception {

    public InsufficientFundsException(String message) {
        this(message, null);
    }

    public InsufficientFundsException(String message, Exception e) {
        super(message, e);
    }
}
