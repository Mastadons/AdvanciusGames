package net.advancius.game.question.trivia;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import net.advancius.AdvanciusGames;
import net.advancius.command.CommandFlags;
import net.advancius.game.GameLanguage;
import net.advancius.game.question.QuestionException;
import net.advancius.game.question.QuestionProvider;
import net.advancius.person.Person;
import net.advancius.placeholder.PlaceholderComponent;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

@Data
public class TriviaQuestionProvider implements QuestionProvider<TriviaQuestion> {

    public final static String QUESTION_URL = "https://opentdb.com/api.php?amount=1&type=multiple&encode=base64";

    public static TriviaQuestionProvider createProviderInstance() {
        return new TriviaQuestionProvider();
    }

    private final String fancyName = "Trivia";
    private final String name = "trivia";

    @Override
    public TriviaQuestion generateQuestion(CommandFlags arguments) {
        try {
            HttpsURLConnection connection = establishConnection();
            JsonObject result = getResult(connection);

            String question = elementToString(result.get("question"));

            List<String> answers = new ArrayList<>();
            result.get("incorrect_answers").getAsJsonArray().forEach(element -> answers.add(elementToString(element)));

            int index = (int)(Math.random() * (answers.size() + 1));
            answers.add(index, elementToString(result.get("correct_answer")));

            return new TriviaQuestion(question, answers, index);
        } catch (Exception exception) { throw new QuestionException(exception); }
    }

    private String elementToString(JsonElement element) {
        return new String(Base64.getDecoder().decode(element.getAsString()));
    }

    private HttpsURLConnection establishConnection() throws IOException {
        URL url = new URL(QUESTION_URL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpsURLConnection.HTTP_OK)
            throw new IOException("Could not establish HTTPS connection to Trivia Questions.", null);

        return connection;
    }

    private JsonObject getResult(HttpsURLConnection connection) throws IOException {
        Scanner scanner = new Scanner(new InputStreamReader(connection.getInputStream()));
        StringBuffer response = new StringBuffer();
        while (scanner.hasNextLine()) response.append(scanner.nextLine());
        scanner.close();
        JsonObject json = new JsonParser().parse(response.toString()).getAsJsonObject();
        JsonArray results = json.get("results").getAsJsonArray();
        return results.get(0).getAsJsonObject();
    }

    @Override
    public void onQuestionSummoned(TriviaQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().trivia.questionSummoned);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
    }

    @Override
    public void onQuestionFinished(TriviaQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().trivia.questionFinished);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
    }

    @Override
    public boolean onQuestionRightAnswer(Person person, TriviaQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().trivia.questionRightAnswer);
        placeholderComponent.replace("question", question);
        placeholderComponent.replace("person", person);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
        return false;
    }

    @Override
    public boolean onQuestionWrongAnswer(Person person, TriviaQuestion question, String answer) {
        return false;
    }
}
