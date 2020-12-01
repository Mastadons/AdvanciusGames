package net.advancius.game.context;

import com.google.gson.JsonObject;
import lombok.Data;
import net.advancius.AdvanciusBungee;
import net.advancius.game.GameManager;
import net.advancius.game.question.Question;
import net.advancius.game.question.QuestionProvider;
import net.advancius.game.statistic.GameScore;
import net.advancius.person.Person;
import net.advancius.person.context.PersonContext;
import net.advancius.statistic.StatisticManager;
import net.advancius.statistic.StatisticScore;
import net.advancius.utils.Metadata;

@Data
public class ChatgameContext extends PersonContext {

    public static ChatgameContext getContext(Person person) {
        return person.getContextManager().getContext(ChatgameContext.class);
    }

    public static void incrementScore(Person person, QuestionProvider questionProvider, boolean correct) {
        person.getContextManager().getContext(ChatgameContext.class).incrementScore(questionProvider, correct);
    }

    public long getRightAnswers(QuestionProvider questionProvider) {
        StatisticScore score = GameManager.RIGHT_ANSWER_STATISTIC.getScoreOrDefault(person.getId(), new GameScore());
        return score.getScore(GameScore.class).getScore(questionProvider);
    }

    public void setRightAnswers(QuestionProvider questionProvider, long value) {
        StatisticScore score = GameManager.RIGHT_ANSWER_STATISTIC.getScoreOrDefault(person.getId(), new GameScore());
        score.getScore(GameScore.class).setScore(questionProvider, value);
    }

    public long getWrongAnswers(QuestionProvider questionProvider) {
        StatisticScore score = GameManager.WRONG_ANSWER_STATISTIC.getScoreOrDefault(person.getId(), new GameScore());
        return score.getScore(GameScore.class).getScore(questionProvider);
    }

    public void setWrongAnswers(QuestionProvider questionProvider, long value) {
        StatisticScore score = GameManager.WRONG_ANSWER_STATISTIC.getScoreOrDefault(person.getId(), new GameScore());
        score.getScore(GameScore.class).setScore(questionProvider, value);
    }

    public void incrementScore(QuestionProvider questionProvider, boolean correct) {
        if (correct) setRightAnswers(questionProvider, getRightAnswers(questionProvider) + 1);
        else setWrongAnswers(questionProvider, getWrongAnswers(questionProvider) + 1);
    }

    public GameScore correctScore() {
        StatisticScore score = GameManager.RIGHT_ANSWER_STATISTIC.getScoreOrDefault(person.getId(), new GameScore());
        return score.getScore(GameScore.class);
    }

    public GameScore incorrectScore() {
        StatisticScore score = GameManager.WRONG_ANSWER_STATISTIC.getScoreOrDefault(person.getId(), new GameScore());
        return score.getScore(GameScore.class);
    }

    @Override
    public JsonObject serializeJson() {
        return new JsonObject();
    }

    @Override
    public void onPersonLoad() {}

    @Override
    public void onPersonSave() {}

    @Override
    public String getName() {
        return "chatgames";
    }
}
