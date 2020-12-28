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
import net.advancius.person.Person;
import net.advancius.person.context.ConnectionContext;

@FlagManager.FlaggedClass
public class StopCommand implements CommandListener {

    @FlagManager.FlaggedMethod(priority = 5, flag = DefinedFlag.GAMES_PLUGIN_LOAD)
    public static void command() {
        AdvanciusBungee.getInstance().getCommandManager().registerListener(new StopCommand());
    }

    @CommandHandler(description = "chatgames.stop")
    public void onCommand(Person person, CommandDescription description, String[] arguments) throws Exception {
        final GameManager GAME_MANAGER = AdvanciusGames.getInstance().getGameManager();

        if (GAME_MANAGER.getQuestion() == null) throw new Exception(GameLanguage.getInstance().noActiveGame);

        GAME_MANAGER.setDuration(0);
        ConnectionContext.sendMessage(person, GameLanguage.getInstance().stopped);
    }
}
