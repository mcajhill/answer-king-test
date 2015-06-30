package answer.king.throwables.exception;

public class InvalidItemPriceException extends AnswerKingException{

    public InvalidItemPriceException() {
        this("Item must have a valid price");
    }

    public InvalidItemPriceException(String message) {
        this(message, null);
    }

    public InvalidItemPriceException(String message, Exception e) {
        super(message, e);
    }
}
