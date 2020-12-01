package net.advancius.game.question.country;

import lombok.Data;
import net.advancius.game.question.Question;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Data
public class CountryQuestion implements Question {

    private final Random random = new Random(UUID.randomUUID().getMostSignificantBits());

    private final String country;
    private final String capital;

    public String getScrambledCapital() {
        String scrambled = "";
        for (String word : capital.split(" ")) {
            List<String> letters = Arrays.asList(word.split(""));
            Collections.shuffle(letters, random);
            String shuffled = "";
            for (String letter : letters) shuffled += letter;
            scrambled += shuffled;
            scrambled += " ";
        }
        return scrambled.trim();
    }

    @Override
    public boolean isAnswer(String message) {
        return capital.equalsIgnoreCase(message);
    }
}
