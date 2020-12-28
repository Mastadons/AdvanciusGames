package net.advancius.game.command;

import net.advancius.AdvanciusBungee;
import net.advancius.command.CommandDescription;
import net.advancius.command.CommandHandler;
import net.advancius.command.CommandListener;
import net.advancius.flag.DefinedFlag;
import net.advancius.flag.FlagManager;
import net.advancius.game.GameConfiguration;
import net.advancius.game.GameLanguage;
import net.advancius.person.Person;
import net.advancius.placeholder.PlaceholderComponent;

import java.io.FileNotFoundException;

@FlagManager.FlaggedClass
public class ReloadCommand implements CommandListener {

    @FlagManager.FlaggedMethod(priority = 5, flag = DefinedFlag.GAMES_PLUGIN_LOAD)
    public static void command() {
        AdvanciusBungee.getInstance().getCommandManager().registerListener(new ReloadCommand());
    }

    @CommandHandler(description = "chatgames.reload")
    public void onCommand(Person person, CommandDescription description, String[] arguments) throws Exception {
        try {
            GameConfiguration.load();
            GameLanguage.load();

            PlaceholderComponent component = new PlaceholderComponent(GameLanguage.getInstance().reload);
            component.translateColor();
            component.send(person);
        } catch (FileNotFoundException exception) {
            throw new Exception("Encountered error reloading", exception);
        }
    }
}
