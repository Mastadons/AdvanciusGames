package net.advancius.game.question.equation;

import lombok.Data;
import net.advancius.game.question.Question;

import java.math.BigDecimal;

@Data
public class EquationQuestion implements Question {

    private final String expression;
    private final BigDecimal answer;

    @Override
    public boolean isAnswer(String message) {
        return message.equalsIgnoreCase(answer.toPlainString());
    }
}
