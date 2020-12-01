package net.advancius.game.question;

public class QuestionException extends RuntimeException {

    public QuestionException(Throwable cause) {
        super(cause);
    }

    public QuestionException(String message, Throwable cause) {
        super(message, cause);
    }
}
