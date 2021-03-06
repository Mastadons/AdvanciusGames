package net.advancius.game.command;

import net.advancius.AdvanciusBungee;
import net.advancius.command.CommandDescription;
import net.advancius.command.CommandHandler;
import net.advancius.command.CommandListener;
import net.advancius.flag.DefinedFlag;
import net.advancius.flag.FlagManager;
import net.advancius.person.Person;
import net.advancius.person.context.ConnectionContext;
import net.advancius.utils.ColorUtils;

@FlagManager.FlaggedClass
public class GameCommand implements CommandListener {

    @FlagManager.FlaggedMethod(priority = 5, flag = DefinedFlag.GAMES_PLUGIN_LOAD)
    public static void command() {
        AdvanciusBungee.getInstance().getCommandManager().registerListener(new GameCommand());
    }

    @CommandHandler(description = "chatgames")
    public void onCommand(Person person, CommandDescription description, String[] arguments) throws Exception {
        ConnectionContext.sendMessage(person, ColorUtils.toTextComponent("&cUnknown subcommand."));
    }
}
