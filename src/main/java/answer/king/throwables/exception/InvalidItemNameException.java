package answer.king.throwables.exception;

public class InvalidItemNameException extends AnswerKingException {

    private static final String MESSAGE = "An item must have a valid name.";


    public InvalidItemNameException() {
        this(MESSAGE);
    }

    public InvalidItemNameException(String message) {
        this(message, null);
    }

    public InvalidItemNameException(String message, Exception e) {
        super(message, e);
    }
}
