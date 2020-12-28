package net.advancius.game.question.scramble;

import lombok.Data;
import net.advancius.AdvanciusGames;
import net.advancius.command.CommandFlags;
import net.advancius.game.GameConfiguration;
import net.advancius.game.GameLanguage;
import net.advancius.game.commons.WordList;
import net.advancius.game.question.QuestionProvider;
import net.advancius.person.Person;
import net.advancius.placeholder.PlaceholderComponent;

import java.io.FileNotFoundException;

@Data
public class ScrambleQuestionProvider implements QuestionProvider<ScrambleQuestion> {

    private final String fancyName = "Scramble";
    private final String name = "scramble";

    private final ScrambleConfiguration configuration;
    private final WordList wordList;

    public static ScrambleQuestionProvider createProviderInstance() throws FileNotFoundException {
        return new ScrambleQuestionProvider(GameConfiguration.getInstance().scramble, WordList.generateList("words.txt", "words.txt"));
    }

    @Override
    public ScrambleQuestion generateQuestion(CommandFlags arguments) {
        int level = getQuestionLevel(arguments);

        if (arguments.hasFlag("answer")) return new ScrambleQuestion(arguments.getFlag("answer").getValue().split(" "));

        String[] words = new String[level];
        for (int i=0; i<level; i++) words[i] = wordList.getRandom();

        return new ScrambleQuestion(words);
    }

    private int getQuestionLevel(CommandFlags arguments) {
        return arguments.hasFlag("level") ? arguments.getFlag("level").asInteger() : configuration.defaultLevel;
    }

    @Override
    public void onQuestionSummoned(ScrambleQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().scramble.questionSummoned);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponent());
    }

    @Override
    public void onQuestionFinished(ScrambleQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().scramble.questionFinished);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponent());
    }

    @Override
    public void onRequestAnswer(Person person, ScrambleQuestion question) {
        PlaceholderComponent component = new PlaceholderComponent(GameLanguage.getInstance().scramble.answer);
        component.replace("question", question);
        component.translateColor();
        component.send(person);
    }

    @Override
    public boolean onQuestionRightAnswer(Person person, ScrambleQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().scramble.questionRightAnswer);
        placeholderComponent.replace("question", question);
        placeholderComponent.replace("person", person);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponent());
        return false;
    }

    @Override
    public boolean onQuestionWrongAnswer(Person person, ScrambleQuestion question, String answer) {
        return false;
    }
}
