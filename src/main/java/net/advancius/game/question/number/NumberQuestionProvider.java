package net.advancius.game.question.number;

import lombok.Data;
import net.advancius.AdvanciusGames;
import net.advancius.command.CommandFlags;
import net.advancius.game.GameConfiguration;
import net.advancius.game.GameLanguage;
import net.advancius.game.question.QuestionProvider;
import net.advancius.person.Person;
import net.advancius.person.context.BungeecordContext;
import net.advancius.placeholder.PlaceholderComponent;

@Data
public class NumberQuestionProvider implements QuestionProvider<NumberQuestion> {

    private final String fancyName = "Number Guess";
    private final String name = "number";

    private final NumberConfiguration configuration;

    public static NumberQuestionProvider createProviderInstance() {
        return new NumberQuestionProvider(GameConfiguration.getInstance().number);
    }

    @Override
    public NumberQuestion generateQuestion(CommandFlags arguments) {
        int minimum0 = arguments.hasFlag("minimum") ? arguments.getFlag("minimum").asInteger() : configuration.defaultMinimum;
        int maximum0 = arguments.hasFlag("maximum") ? arguments.getFlag("maximum").asInteger() : configuration.defaultMaximum;

        int minimum = Math.min(minimum0, maximum0);
        int maximum = Math.max(minimum0, maximum0);

        return new NumberQuestion(minimum, maximum, generateNumber(minimum, maximum));
    }

    private int generateNumber(int minimum, int maximum) {
        return (int) ((Math.random() * (maximum - minimum)) + minimum);
    }

    @Override
    public void onQuestionSummoned(NumberQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().number.questionSummoned);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
    }

    @Override
    public void onQuestionFinished(NumberQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().number.questionFinished);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
    }

    @Override
    public boolean onQuestionRightAnswer(Person person, NumberQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().number.questionRightAnswer);
        placeholderComponent.replace("question", question);
        placeholderComponent.replace("person", person);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
        return false;
    }

    @Override
    public boolean onQuestionWrongAnswer(Person person, NumberQuestion question, String answer) {
        try {
            int numberAnswer = Integer.parseInt(answer);
            if (question.getNumber() < numberAnswer) BungeecordContext.sendMessage(person, "&cGuess a lower number!");
            if (question.getNumber() > numberAnswer) BungeecordContext.sendMessage(person, "&cGuess a higher number!");
            return true;
        } catch (NumberFormatException exception) { return false; }
    }
}
