package net.advancius.game.question.hangman;

import lombok.Data;
import net.advancius.game.question.Question;
import org.apache.commons.lang3.StringUtils;

@Data
public class HangmanQuestion implements Question {

    private final String phrase;

    @Override
    public boolean isAnswer(String message) {
        return message.equalsIgnoreCase(phrase);
    }

    public String blankString() {
        String[] phraseComponents = phrase.split(" ");
        String[] blankComponents = new String[phraseComponents.length];

        for (int i=0; i<phraseComponents.length; i++) {
            blankComponents[i] = StringUtils.repeat('_', phraseComponents[i].length());
        }
        return String.join(" ", blankComponents);
    }
}
