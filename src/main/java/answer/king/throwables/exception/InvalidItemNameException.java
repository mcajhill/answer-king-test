package answer.king.throwables.exception;

public class InvalidItemNameException extends AnswerKingException {

    public InvalidItemNameException() {
        this("Item must have a valid name");
    }

    public InvalidItemNameException(String message) {
        this(message, null);
    }

    public InvalidItemNameException(String message, Exception e) {
        super(message, e);
    }
}
