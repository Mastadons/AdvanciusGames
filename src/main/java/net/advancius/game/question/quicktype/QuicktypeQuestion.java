package net.advancius.game.question.quicktype;

import lombok.Data;
import net.advancius.game.question.Question;

@Data
public class QuicktypeQuestion implements Question {

    private final String[] words;

    @Override
    public boolean isAnswer(String message) {
        return message.equalsIgnoreCase(String.join(" ", words));
    }

    public String getSentence() {
        String[] components = new String[words.length];
        for (int i=0; i<components.length; i++) {
            components[i] = (words[i].substring(0, 1).toUpperCase() + words[i].substring(1));
        }
        return String.join(" ", components);
    }
}
