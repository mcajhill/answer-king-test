package answer.king.throwables.exception;

public class OrderAlreadyPaidException extends AnswerKingException {

    private static final String MESSAGE = "The order amount has already been paid.";


    public OrderAlreadyPaidException() {
        this(MESSAGE);
    }

    public OrderAlreadyPaidException(String message) {
        this(message, null);
    }

    public OrderAlreadyPaidException(String message, Exception e) {
        super(message, e);
    }
}
