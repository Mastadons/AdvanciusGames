package net.advancius.game.question.country;

import lombok.Data;
import net.advancius.AdvanciusGames;
import net.advancius.command.CommandFlags;
import net.advancius.file.FileManager;
import net.advancius.game.GameLanguage;
import net.advancius.game.question.QuestionException;
import net.advancius.game.question.QuestionProvider;
import net.advancius.person.Person;
import net.advancius.placeholder.PlaceholderComponent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Data
public class CountryQuestionProvider implements QuestionProvider<CountryQuestion> {

    private final String fancyName = "Country";
    private final String name = "country";

    private final List<String> countryList;
    private final List<String> capitalList;

    public static CountryQuestionProvider createProviderInstance() throws FileNotFoundException {
        File countriesFile = FileManager.getServerFile("countries.txt", "countries.txt");

        List<String> countryList = new ArrayList<>();
        List<String> capitalList = new ArrayList<>();

        Scanner scanner = new Scanner(new FileReader(countriesFile));
        while (scanner.hasNextLine()) {
            String[] components = scanner.nextLine().split(":");

            countryList.add(components[0]);
            capitalList.add(components[1]);
        }
        scanner.close();
        return new CountryQuestionProvider(countryList, capitalList);
    }

    @Override
    public CountryQuestion generateQuestion(CommandFlags arguments) {
        if (arguments.hasFlag("country")) {
            String country = arguments.getFlag("country").getValue();
            String capital = arguments.getFlag("capital", new QuestionException("Must provide capital argument with country argument.", null)).getValue();

            return new CountryQuestion(country, capital);
        }

        int index = (int)(Math.random() * countryList.size());
        return new CountryQuestion(countryList.get(index), capitalList.get(index));
    }

    @Override
    public void onQuestionSummoned(CountryQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().country.questionSummoned);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
    }

    @Override
    public void onQuestionFinished(CountryQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().country.questionFinished);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
    }

    @Override
    public boolean onQuestionRightAnswer(Person person, CountryQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().country.questionRightAnswer);
        placeholderComponent.replace("question", question);
        placeholderComponent.replace("person", person);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
        return false;
    }

    @Override
    public boolean onQuestionWrongAnswer(Person person, CountryQuestion question, String answer) {
        return false;
    }
}
