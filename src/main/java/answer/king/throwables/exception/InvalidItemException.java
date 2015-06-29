package answer.king.throwables.exception;

public class InvalidItemException extends Exception {

    public InvalidItemException(String message) {
        this(message, null);
    }

    public InvalidItemException(String message, Exception e) {
        super(message, e);
    }
}
