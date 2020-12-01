package net.advancius.game;

import lombok.Data;
import net.advancius.AdvanciusBungee;
import net.advancius.AdvanciusGames;
import net.advancius.AdvanciusLogger;
import net.advancius.command.CommandFlags;
import net.advancius.communication.CommunicationPacket;
import net.advancius.communication.client.Client;
import net.advancius.flag.DefinedFlag;
import net.advancius.flag.FlagManager;
import net.advancius.game.question.QuestionProvider;
import net.advancius.game.question.SummonedQuestion;
import net.advancius.game.question.country.CountryQuestionProvider;
import net.advancius.game.question.equation.EquationQuestionProvider;
import net.advancius.game.question.number.NumberQuestionProvider;
import net.advancius.game.question.quicktype.QuicktypeQuestionProvider;
import net.advancius.game.question.scramble.ScrambleQuestionProvider;
import net.advancius.game.question.variable.VariableQuestionProvider;
import net.advancius.game.statistic.GameScore;
import net.advancius.person.Person;
import net.advancius.person.context.BungeecordContext;
import net.advancius.placeholder.PlaceholderComponent;
import net.advancius.protocol.Protocol;
import net.advancius.statistic.Statistic;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
@FlagManager.FlaggedClass
public class GameManager {

    public static Statistic<GameScore> RIGHT_ANSWER_STATISTIC;
    public static Statistic<GameScore> WRONG_ANSWER_STATISTIC;

    @FlagManager.FlaggedMethod(flag = DefinedFlag.GAMES_PLUGIN_LOAD, priority = 500)
    public static void gameManager() throws FileNotFoundException {
        GameManager instance = new GameManager();

        instance.getQuestionProviderList().add(CountryQuestionProvider.createProviderInstance());
        instance.getQuestionProviderList().add(EquationQuestionProvider.createProviderInstance());
        //instance.getQuestionProviderList().add(HangmanQuestionProvider.createProviderInstance());
        instance.getQuestionProviderList().add(NumberQuestionProvider.createProviderInstance());
        instance.getQuestionProviderList().add(QuicktypeQuestionProvider.createProviderInstance());
        instance.getQuestionProviderList().add(ScrambleQuestionProvider.createProviderInstance());
        //instance.getQuestionProviderList().add(TriviaQuestionProvider.createProviderInstance());
        instance.getQuestionProviderList().add(VariableQuestionProvider.createProviderInstance());

        RIGHT_ANSWER_STATISTIC = AdvanciusBungee.getInstance().getStatisticManager().registerStatistic("chatgames", "right-answers", GameScore.class);
        WRONG_ANSWER_STATISTIC = AdvanciusBungee.getInstance().getStatisticManager().registerStatistic("chatgames", "wrong-answers", GameScore.class);

        AdvanciusGames.getInstance().setGameManager(instance);

        instance.cooldown = GameConfiguration.getInstance().firstSummonDelay;
        ProxyServer.getInstance().getScheduler().schedule(AdvanciusGames.getInstance(), instance::summonerTick, 0L, 1L, TimeUnit.SECONDS);
    }

    private final List<QuestionProvider> questionProviderList = new ArrayList<>();

    private SummonedQuestion queuedQuestion;
    private SummonedQuestion question;

    private boolean paused;
    private int cooldown = -1;
    private int duration = -1;

    public QuestionProvider getQuestionProvider(String name) {
        for (QuestionProvider questionProvider : questionProviderList) if (questionProvider.getName().equalsIgnoreCase(name)) return questionProvider;
        return null;
    }

    public QuestionProvider getRandomQuestionProvider() {
        return questionProviderList.get((int)(Math.random() * questionProviderList.size()));
    }

    private void summonerTick() {
        if (paused) return;

        if (canSummon() && cooldown != -1 && cooldown-- <= 0) {
            question = generateNextQuestion();
            question.getQuestionProvider().onQuestionSummoned(question.getQuestion());
            duration = GameConfiguration.getInstance().duration;
            cooldown = -1;
        }
        if (duration != -1 && duration-- <= 0) {
            if (question != null) question.getQuestionProvider().onQuestionFinished(question.getQuestion());

            cooldown = GameConfiguration.getInstance().cooldown;
            duration = -1;
        }
    }

    private boolean canSummon() {
        int onlineCount = AdvanciusBungee.getInstance().getPersonManager().getOnlinePersons().size();
        return queuedQuestion != null || onlineCount >= GameConfiguration.getInstance().minimumPlayers;
    }

    private SummonedQuestion generateNextQuestion() {
        if (queuedQuestion != null) {
            SummonedQuestion nextQuestion = queuedQuestion;
            queuedQuestion = null;
            return nextQuestion;
        }
        else {
            QuestionProvider questionProvider = getRandomQuestionProvider();
            return new SummonedQuestion(questionProvider.generateQuestion(new CommandFlags()), questionProvider);
        }
    }

    public void sendRewards(Person person) {
        String bungeeRewardCommand = getRewardCommand(person, GameConfiguration.getInstance().bungeeRewardCommand);
        String spigotRewardCommand = getRewardCommand(person, GameConfiguration.getInstance().spigotRewardCommand);

        AdvanciusLogger.info("Running bungee chat game reward command: " + bungeeRewardCommand);
        ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), bungeeRewardCommand);

        AdvanciusLogger.info("Running spigot chat game reward command: " + spigotRewardCommand);
        CommunicationPacket communicationPacket = CommunicationPacket.generatePacket(Protocol.SERVER_CROSS_COMMAND);
        communicationPacket.getMetadata().setMetadata("command", spigotRewardCommand);

        BungeecordContext bungeecordContext = person.getContextManager().getContext(BungeecordContext.class);
        Client client = AdvanciusBungee.getInstance().getCommunicationManager().getClient(bungeecordContext.getServer());
        client.sendPacket(communicationPacket);
    }

    public String getRewardCommand(Person person, String command) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(command);
        placeholderComponent.replace("person", person);
        return placeholderComponent.getText();
    }

    public void broadcastMessage(TextComponent textComponent) {
        AdvanciusBungee.getInstance().getPersonManager().broadcastMessage(textComponent);
    }
}