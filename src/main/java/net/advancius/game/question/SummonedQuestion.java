package net.advancius.game.question;

import lombok.Data;

@Data
public class SummonedQuestion {

    private final Question question;
    private final QuestionProvider questionProvider;

    private boolean answered;
}
