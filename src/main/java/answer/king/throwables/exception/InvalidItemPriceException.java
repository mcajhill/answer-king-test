package answer.king.throwables.exception;

public class InvalidItemPriceException extends AnswerKingException{

    private static final String MESSAGE = "An item must have a valid price.";


    public InvalidItemPriceException() {
        this(MESSAGE);
    }

    public InvalidItemPriceException(String message) {
        this(message, null);
    }

    public InvalidItemPriceException(String message, Exception e) {
        super(message, e);
    }
}
