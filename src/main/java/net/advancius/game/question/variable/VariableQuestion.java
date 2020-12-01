package net.advancius.game.question.variable;

import lombok.Data;
import net.advancius.game.question.Question;

import java.math.BigDecimal;

@Data
public class VariableQuestion implements Question {

    private final String expression;
    private final BigDecimal expressionAnswer;

    private final int variableAnswer;

    @Override
    public boolean isAnswer(String message) {
        try {
            return Integer.valueOf(message) == variableAnswer;
        } catch (NumberFormatException exception) { return false; }
    }
}
