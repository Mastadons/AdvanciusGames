package net.advancius.game.command;

import net.advancius.AdvanciusBungee;
import net.advancius.AdvanciusGames;
import net.advancius.command.CommandDescription;
import net.advancius.command.CommandFlagParser;
import net.advancius.command.CommandFlags;
import net.advancius.command.CommandHandler;
import net.advancius.command.CommandListener;
import net.advancius.flag.DefinedFlag;
import net.advancius.flag.FlagManager;
import net.advancius.game.GameConfiguration;
import net.advancius.game.GameLanguage;
import net.advancius.game.GameManager;
import net.advancius.game.question.Question;
import net.advancius.game.question.QuestionProvider;
import net.advancius.game.question.SummonedQuestion;
import net.advancius.game.statistic.GameScore;
import net.advancius.person.Person;
import net.advancius.person.context.BungeecordContext;
import net.advancius.person.context.PermissionContext;
import net.advancius.placeholder.PlaceholderComponent;
import net.advancius.utils.ColorUtils;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.UUID;

@FlagManager.FlaggedClass
public class GameCommand implements CommandListener {

    @FlagManager.FlaggedMethod(priority = 0, flag = DefinedFlag.GAMES_PLUGIN_LOAD)
    public static void command() {
        AdvanciusBungee.getInstance().getCommandManager().addDescription(GameConfiguration.getInstance().commandDescription);
        AdvanciusBungee.getInstance().getCommandManager().registerListener(new GameCommand());
    }

    @CommandHandler(description = "chatgames")
    public void onCommand(Person person, CommandDescription description, String[] arguments) throws Exception {
        GameManager gameManager = AdvanciusGames.getInstance().getGameManager();

        if (arguments.length == 0) {
            PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().info);
            placeholderComponent.translateColor();

            BungeecordContext bungeecordContext = person.getContextManager().getContext("bungeecord");
            bungeecordContext.sendMessage(placeholderComponent.toTextComponentUnsafe());
            return;
        }

        if (arguments[0].equalsIgnoreCase("reload")) {
            if (!PermissionContext.hasPermission(person, "advancius.chatgames.reload"))
                throw new Exception("You do not have permission for this.");
            try {
                GameConfiguration.load();
                GameLanguage.load();

                PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().reload);
                placeholderComponent.translateColor();

                BungeecordContext bungeecordContext = person.getContextManager().getContext("bungeecord");
                bungeecordContext.sendMessage(placeholderComponent.toTextComponentUnsafe());
            } catch (FileNotFoundException exception) {
                throw new Exception("Encountered error reloading", exception);
            }
        }
        else if (arguments[0].equalsIgnoreCase("cooldown")) {
            if (!PermissionContext.hasPermission(person, "advancius.chatgames.cooldown"))
                throw new Exception("You do not have permission for this.");
            if (gameManager.getCooldown() == -1) {
                BungeecordContext.sendMessage(person, "&cThe chat games are not on cooldown.");
            }
            else BungeecordContext.sendMessage(person, "&cThe chat games are on cooldown for " + gameManager.getCooldown() + " seconds.");
        }
        else if (arguments[0].equalsIgnoreCase("duration")) {
            if (!PermissionContext.hasPermission(person, "advancius.chatgames.duration"))
                throw new Exception("You do not have permission for this.");
            if (gameManager.getDuration() == -1) {
                BungeecordContext.sendMessage(person, "&cThere is no chat game currently active.");
            }
            else BungeecordContext.sendMessage(person, "&cThe current chat game will be active for " + gameManager.getDuration() + " seconds.");
        }
        else if (arguments[0].equalsIgnoreCase("score")) {
            if (!PermissionContext.hasPermission(person, "advancius.chatgames.score"))
                throw new Exception("You do not have permission for this.");

            String name = person.getContextManager().getContext(BungeecordContext.class).getProxiedPlayer().getDisplayName();
            UUID id = person.getId();
            if (arguments.length > 1) {
                UUID argumentId = AdvanciusBungee.getInstance().getPersonManager().getOfflinePerson(arguments[1]);
                if (argumentId == null) throw new Exception("Unknown player.");
                id = argumentId;
                name = arguments[1];
            }
            sendScores(person, name, id);
        }
        else if (arguments[0].equalsIgnoreCase("summon")) {
            if (!PermissionContext.hasPermission(person, "advancius.chatgames.summon"))
                throw new Exception("You do not have permission for this.");
            SummonedQuestion summonedQuestion = null;
            if (arguments.length == 1) {
                QuestionProvider questionProvider = gameManager.getRandomQuestionProvider();
                Question question = questionProvider.generateQuestion(new CommandFlags());
                summonedQuestion = new SummonedQuestion(question, questionProvider);
            }
            else {
                CommandFlags commandFlags = CommandFlagParser.getCommandFlags(String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length)));
                QuestionProvider questionProvider = gameManager.getRandomQuestionProvider();
                if (commandFlags.hasFlag("type"))
                    questionProvider = gameManager.getQuestionProvider(commandFlags.getFlag("type").getValue());
                Question question = questionProvider.generateQuestion(commandFlags);
                summonedQuestion = new SummonedQuestion(question, questionProvider);
            }
            if (summonedQuestion.getQuestion() == null) throw new Exception("Failed to generate question.");

            gameManager.setQueuedQuestion(summonedQuestion);
            if (gameManager.getDuration() > 0) gameManager.setDuration(0);
            gameManager.setCooldown(0);
        }
        else {
            BungeecordContext bungeecordContext = person.getContextManager().getContext("bungeecord");
            bungeecordContext.sendMessage(ColorUtils.toTextComponent("&cUnknown subcommand."));
        }
    }

    private void sendScores(Person person, String name, UUID id) {
        GameManager gameManager = AdvanciusGames.getInstance().getGameManager();
        BungeecordContext.sendMessage(person, "&6Chatgame scores for " + name);

        GameScore gameScore = GameManager.RIGHT_ANSWER_STATISTIC.getScoreOrDefault(id, new GameScore()).getScore(GameScore.class);

        for (QuestionProvider questionProvider : gameManager.getQuestionProviderList()) {
            PlaceholderComponent placeholderComponent = new PlaceholderComponent("&e{game}: &a{score} points");
            placeholderComponent.replace("game", questionProvider.getFancyName());
            placeholderComponent.replace("score", gameScore.getScore(questionProvider));
            placeholderComponent.translateColor();
            BungeecordContext.sendMessage(person, placeholderComponent.toTextComponentUnsafe());
        }
        PlaceholderComponent placeholderComponent = new PlaceholderComponent("&e&lTotal score: &a&l{score} points");
        placeholderComponent.replace("score", gameScore.total());
        placeholderComponent.translateColor();
        BungeecordContext.sendMessage(person, placeholderComponent.toTextComponentUnsafe());
    }

    /*
    SummonCommand
StopCommand
PauseCommand
AnswerCommand
ReloadCommand
CooldownCommand
DurationCommand
IgnoreCommand
ScoreCommand
     */
}
