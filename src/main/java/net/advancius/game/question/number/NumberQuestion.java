package net.advancius.game.question.number;

import lombok.Data;
import net.advancius.game.question.Question;

@Data
public class NumberQuestion implements Question {

    private final int minimumNumber;
    private final int maximumNumber;

    private final int number;

    @Override
    public boolean isAnswer(String message) {
        try {
            return Integer.valueOf(message) == number;
        } catch (NumberFormatException exception) { return false; }
    }
}
