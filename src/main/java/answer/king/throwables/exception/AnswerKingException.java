package answer.king.throwables.exception;

public abstract class AnswerKingException extends Exception {

    public AnswerKingException(String message, Exception e) {
        super(message, e);
    }
}
