package net.advancius.game.question.hangman;

import lombok.Data;
import net.advancius.AdvanciusGames;
import net.advancius.command.CommandFlags;
import net.advancius.game.GameLanguage;
import net.advancius.game.commons.WordList;
import net.advancius.game.question.QuestionProvider;
import net.advancius.person.Person;
import net.advancius.placeholder.PlaceholderComponent;

import java.io.FileNotFoundException;

@Data
public class HangmanQuestionProvider implements QuestionProvider<HangmanQuestion> {

    private final String fancyName = "Hangman";
    private final String name = "hangman";

    private final WordList wordList;

    public static HangmanQuestionProvider createProviderInstance() throws FileNotFoundException {
        return new HangmanQuestionProvider(WordList.generateList("words.txt", "words.txt"));
    }

    @Override
    public HangmanQuestion generateQuestion(CommandFlags arguments) {
        if (arguments.hasFlag("answer")) return new HangmanQuestion(arguments.getFlag("answer").getValue());

        return new HangmanQuestion(wordList.getRandom());
    }

    @Override
    public void onQuestionSummoned(HangmanQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().hangman.questionSummoned);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
    }

    @Override
    public void onQuestionFinished(HangmanQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().hangman.questionFinished);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
    }

    @Override
    public boolean onQuestionRightAnswer(Person person, HangmanQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().hangman.questionRightAnswer);
        placeholderComponent.replace("question", question);
        placeholderComponent.replace("person", person);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
        return false;
    }

    @Override
    public boolean onQuestionWrongAnswer(Person person, HangmanQuestion question, String answer) {
        return false;
    }
}
