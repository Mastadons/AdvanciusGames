package net.advancius.game.question.trivia;

import lombok.Data;
import net.advancius.game.question.Question;

import java.util.List;

@Data
public class TriviaQuestion implements Question {

    private final String question;
    private final List<String> answers;
    private final int correctAnswer;

    @Override
    public boolean isAnswer(String message) {
        return false;
    }

    public String getAnswer() {
        return answers.get(correctAnswer);
    }
}
