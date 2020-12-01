package net.advancius.game.question.scramble;

import lombok.Data;
import net.advancius.game.question.Question;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Data
public class ScrambleQuestion implements Question {

    private final Random random = new Random(UUID.randomUUID().getMostSignificantBits());

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

    public String getScrambledSentence() {
        String scrambled = "";
        for (String word : getSentence().split(" ")) {
            List<String> letters = Arrays.asList(word.split(""));
            Collections.shuffle(letters, random);
            String shuffled = "";
            for (String letter : letters) shuffled += letter;
            scrambled += shuffled;
            scrambled += " ";
        }
        return scrambled.trim();
    }
}
