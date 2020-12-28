package net.advancius.game.command;

import net.advancius.AdvanciusBungee;
import net.advancius.AdvanciusGames;
import net.advancius.command.CommandDescription;
import net.advancius.command.CommandHandler;
import net.advancius.command.CommandListener;
import net.advancius.flag.DefinedFlag;
import net.advancius.flag.FlagManager;
import net.advancius.game.GameLanguage;
import net.advancius.game.GameManager;
import net.advancius.game.question.SummonedQuestion;
import net.advancius.person.Person;

@FlagManager.FlaggedClass
public class AnswerCommand implements CommandListener {

    @FlagManager.FlaggedMethod(priority = 5, flag = DefinedFlag.GAMES_PLUGIN_LOAD)
    public static void command() {
        AdvanciusBungee.getInstance().getCommandManager().registerListener(new AnswerCommand());
    }

    @CommandHandler(description = "chatgames.answer")
    public void onCommand(Person person, CommandDescription description, String[] arguments) throws Exception {
        final GameManager GAME_MANAGER = AdvanciusGames.getInstance().getGameManager();

        if (GAME_MANAGER.getQuestion() == null) throw new Exception(GameLanguage.getInstance().noActiveGame);

        SummonedQuestion question = GAME_MANAGER.getQuestion();
        question.getQuestionProvider().onRequestAnswer(person, question.getQuestion());
    }
}
