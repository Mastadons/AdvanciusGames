package net.advancius.game.question.quicktype;

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
public class QuicktypeQuestionProvider implements QuestionProvider<QuicktypeQuestion> {

    private final String fancyName = "Quick Type";
    private final String name = "quicktype";

    private final QuicktypeConfiguration configuration;
    private final WordList wordList;

    public static QuicktypeQuestionProvider createProviderInstance() throws FileNotFoundException {
        return new QuicktypeQuestionProvider(GameConfiguration.getInstance().quicktype, WordList.generateList("words.txt", "words.txt"));
    }

    @Override
    public QuicktypeQuestion generateQuestion(CommandFlags arguments) {
        int level = getQuestionLevel(arguments);

        String[] words = new String[level];
        for (int i=0; i<level; i++) words[i] = wordList.getRandom();

        return new QuicktypeQuestion(words);
    }

    private int getQuestionLevel(CommandFlags arguments) {
        return arguments.hasFlag("level") ? arguments.getFlag("level").asInteger() : configuration.defaultLevel;
    }

    @Override
    public void onQuestionSummoned(QuicktypeQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().quicktype.questionSummoned);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponent());
    }

    @Override
    public void onQuestionFinished(QuicktypeQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().quicktype.questionFinished);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponent());
    }

    @Override
    public void onRequestAnswer(Person person, QuicktypeQuestion question) {
        PlaceholderComponent component = new PlaceholderComponent(GameLanguage.getInstance().quicktype.answer);
        component.replace("question", question);
        component.translateColor();
        component.send(person);
    }

    @Override
    public boolean onQuestionRightAnswer(Person person, QuicktypeQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().quicktype.questionRightAnswer);
        placeholderComponent.replace("question", question);
        placeholderComponent.replace("person", person);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponent());
        return false;
    }

    @Override
    public boolean onQuestionWrongAnswer(Person person, QuicktypeQuestion question, String answer) {
        return false;
    }
}
