package net.advancius.game.question.trivia;

import lombok.Data;
import net.advancius.game.question.Question;

import java.util.List;

@Data
public class TriviaQuestion implements Question {

    public static final String[] ANSWER_LETTERS = {"a", "b", "c", "d"};
    public static final String[] ANSWER_NUMBERS = {"1", "2", "3", "4"};

    private final String question;
    private final List<String> answers;
    private final int correctAnswer;

    @Override
    public boolean isAnswer(String message) {
        return message.equalsIgnoreCase(ANSWER_LETTERS[correctAnswer]) || message.equalsIgnoreCase(ANSWER_NUMBERS[correctAnswer]);
    }

    public String getAnswer() {
        return answers.get(correctAnswer);
    }

    public String getAnswerLetter() {
        return ANSWER_LETTERS[correctAnswer];
    }
}
