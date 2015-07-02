package answer.king.throwables.exception;

public class OrderDoesNotExistException extends AnswerKingException {

    private static final String MESSAGE = "You cannot pay for a non-existent order.";


    public OrderDoesNotExistException() {
        this(MESSAGE);
    }

    public OrderDoesNotExistException(String message) {
        this(message, null);
    }

    public OrderDoesNotExistException(String message, Exception e) {
        super(message, e);
    }
}
