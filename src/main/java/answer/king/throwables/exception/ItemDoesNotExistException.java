package answer.king.throwables.exception;

public class ItemDoesNotExistException extends AnswerKingException {

    private static final String MESSAGE = "You cannot add a non-existent item to this order.";


    public ItemDoesNotExistException() {
        this(MESSAGE);
    }

    public ItemDoesNotExistException(String message) {
        this(message, null);
    }

    public ItemDoesNotExistException(String message, Exception e) {
        super(message, e);
    }
}
