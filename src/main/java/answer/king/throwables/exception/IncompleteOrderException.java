package answer.king.throwables.exception;

public class IncompleteOrderException extends AnswerKingException {

    private static final String MESSAGE = "An order must be complete before processing can continue.";


    public IncompleteOrderException() {
        this(MESSAGE);
    }

    public IncompleteOrderException(String message) {
        this(message, null);
    }

    public IncompleteOrderException(String message, Exception e) {
        super(message, e);
    }
}
