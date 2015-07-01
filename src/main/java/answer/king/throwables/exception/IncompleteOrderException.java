package answer.king.throwables.exception;

public class IncompleteOrderException extends AnswerKingException {

    public IncompleteOrderException() {
        this("Processing of the order failed: it must be complete");
    }

    public IncompleteOrderException(String message) {
        this(message, null);
    }

    public IncompleteOrderException(String message, Exception e) {
        super(message, e);
    }
}
