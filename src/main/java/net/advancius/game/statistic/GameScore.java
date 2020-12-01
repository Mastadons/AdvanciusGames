package net.advancius.game.statistic;

import net.advancius.game.question.QuestionProvider;

import java.util.HashMap;
import java.util.Map;

public class GameScore {

    public Map<String, Long> score = new HashMap<>();

    public long getScore(QuestionProvider questionProvider) {
        return getScore(questionProvider.getName());
    }

    public long getScore(String providerName) {
        return score.getOrDefault(providerName, 0L);
    }

    public void setScore(QuestionProvider questionProvider, long value) {
        score.put(questionProvider.getName(), value);
    }

    public long total() {
        return score.values().stream().reduce(0L, Long::sum);
    }

    public long country() { return getScore("country"); }
    public long equation() { return getScore("equation"); }
    public long hangman() { return getScore("hangman"); }
    public long number() { return getScore("number"); }
    public long quicktype() { return getScore("quicktype"); }
    public long scramble() { return getScore("scramble"); }
    public long trivia() { return getScore("trivia"); }
    public long variable() { return getScore("variable"); }
}
