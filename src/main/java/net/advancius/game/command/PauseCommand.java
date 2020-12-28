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
public class PauseCommand implements CommandListener {

    @FlagManager.FlaggedMethod(priority = 5, flag = DefinedFlag.GAMES_PLUGIN_LOAD)
    public static void command() {
        AdvanciusBungee.getInstance().getCommandManager().registerListener(new PauseCommand());
    }

    @CommandHandler(description = "chatgames.pause")
    public void onCommand(Person person, CommandDescription description, String[] arguments) throws Exception {
        final GameManager GAME_MANAGER = AdvanciusGames.getInstance().getGameManager();

        GAME_MANAGER.setPaused(!GAME_MANAGER.isPaused());
        ConnectionContext.sendMessage(person, GAME_MANAGER.isPaused() ? GameLanguage.getInstance().paused : GameLanguage.getInstance().resumed);
    }
}
