package net.advancius.game.command;

import net.advancius.AdvanciusBungee;
import net.advancius.AdvanciusGames;
import net.advancius.AdvanciusLang;
import net.advancius.command.CommandDescription;
import net.advancius.command.CommandHandler;
import net.advancius.command.CommandListener;
import net.advancius.flag.DefinedFlag;
import net.advancius.flag.FlagManager;
import net.advancius.game.GameLanguage;
import net.advancius.game.GameManager;
import net.advancius.game.question.QuestionProvider;
import net.advancius.game.statistic.GameScore;
import net.advancius.person.Person;
import net.advancius.person.context.ConnectionContext;
import net.advancius.placeholder.PlaceholderComponent;

import java.util.UUID;

@FlagManager.FlaggedClass
public class ScoreCommand implements CommandListener {

    @FlagManager.FlaggedMethod(priority = 5, flag = DefinedFlag.GAMES_PLUGIN_LOAD)
    public static void command() {
        AdvanciusBungee.getInstance().getCommandManager().registerListener(new ScoreCommand());
    }

    @CommandHandler(description = "chatgames.score")
    public void onCommand(Person person, CommandDescription description, String[] arguments) throws Exception {
        String targetName = person.getContextManager().getContext(ConnectionContext.class).getConnectionName();
        UUID id = person.getId();
        if (arguments.length == 1) {
            UUID argumentId = AdvanciusBungee.getInstance().getPersonManager().getOfflinePerson(arguments[0]);
            if (argumentId == null) throw new Exception(AdvanciusLang.getInstance().unknownPlayer);
            id = argumentId;
            targetName = arguments[0];
        }
        sendScores(person, targetName, id);
    }

    private void sendScores(Person reader, String name, UUID id) {
        final GameManager GAME_MANAGER = AdvanciusGames.getInstance().getGameManager();

        PlaceholderComponent headerComponent = new PlaceholderComponent(GameLanguage.getInstance().scoreHeader);
        headerComponent.replace("name", name);
        headerComponent.translateColor();
        headerComponent.send(reader);

        GameScore gameScore = GameManager.RIGHT_ANSWER_STATISTIC.getScoreOrDefault(id, new GameScore()).getScore(GameScore.class);

        for (QuestionProvider questionProvider : GAME_MANAGER.getQuestionProviderList()) {
            PlaceholderComponent lineComponent = new PlaceholderComponent(GameLanguage.getInstance().scoreLine);
            lineComponent.replace("game", questionProvider.getFancyName());
            lineComponent.replace("score", gameScore.getScore(questionProvider));
            lineComponent.translateColor();
            lineComponent.send(reader);
        }

        PlaceholderComponent footerComponent = new PlaceholderComponent(GameLanguage.getInstance().scoreFooter);
        footerComponent.replace("name", name);
        footerComponent.replace("total", gameScore.total());
        footerComponent.translateColor();
        footerComponent.send(reader);
    }
}
