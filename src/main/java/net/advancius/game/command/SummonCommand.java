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
import net.advancius.game.GameLanguage;
import net.advancius.game.GameManager;
import net.advancius.game.question.Question;
import net.advancius.game.question.QuestionProvider;
import net.advancius.game.question.SummonedQuestion;
import net.advancius.person.Person;

import java.util.Arrays;

@FlagManager.FlaggedClass
public class SummonCommand implements CommandListener {

    @FlagManager.FlaggedMethod(priority = 5, flag = DefinedFlag.GAMES_PLUGIN_LOAD)
    public static void command() {
        AdvanciusBungee.getInstance().getCommandManager().registerListener(new SummonCommand());
    }

    @CommandHandler(description = "chatgames.summon")
    public void onCommand(Person person, CommandDescription description, String[] arguments) throws Exception {
        final GameManager GAME_MANAGER = AdvanciusGames.getInstance().getGameManager();

        SummonedQuestion summonedQuestion;
        if (arguments.length == 0) {
            QuestionProvider questionProvider = GAME_MANAGER.getRandomQuestionProvider();
            Question question = questionProvider.generateQuestion(new CommandFlags());
            summonedQuestion = new SummonedQuestion(question, questionProvider);
        }
        else {
            CommandFlags commandFlags = CommandFlagParser.getCommandFlags(String.join(" ", arguments));
            QuestionProvider questionProvider = GAME_MANAGER.getRandomQuestionProvider();
            if (commandFlags.hasFlag("type"))
                questionProvider = GAME_MANAGER.getQuestionProvider(commandFlags.getFlag("type").getValue());
            Question question = questionProvider.generateQuestion(commandFlags);
            summonedQuestion = new SummonedQuestion(question, questionProvider);
        }
        if (summonedQuestion.getQuestion() == null) throw new Exception("Failed to generate question.");

        GAME_MANAGER.setQueuedQuestion(summonedQuestion);
        if (GAME_MANAGER.getDuration() > 0) GAME_MANAGER.setDuration(0);
        GAME_MANAGER.setCooldown(0);
    }
}
