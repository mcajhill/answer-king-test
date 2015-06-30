package answer.king.throwables.exception;

public class InsufficientFundsException extends AnswerKingException {

    public InsufficientFundsException() {
        this("The payment must cover the cost of the order.");
    }

    public InsufficientFundsException(String message) {
        this(message, null);
    }

    public InsufficientFundsException(String message, Exception e) {
        super(message, e);
    }
}
