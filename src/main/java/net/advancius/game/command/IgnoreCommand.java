package net.advancius.game.command;

import net.advancius.AdvanciusBungee;
import net.advancius.command.CommandDescription;
import net.advancius.command.CommandHandler;
import net.advancius.command.CommandListener;
import net.advancius.flag.DefinedFlag;
import net.advancius.flag.FlagManager;
import net.advancius.game.GameLanguage;
import net.advancius.game.context.ChatgameContext;
import net.advancius.person.Person;
import net.advancius.person.context.ConnectionContext;

@FlagManager.FlaggedClass
public class IgnoreCommand implements CommandListener {

    @FlagManager.FlaggedMethod(priority = 5, flag = DefinedFlag.GAMES_PLUGIN_LOAD)
    public static void command() {
        AdvanciusBungee.getInstance().getCommandManager().registerListener(new IgnoreCommand());
    }

    @CommandHandler(description = "chatgames.ignore")
    public void onCommand(Person person, CommandDescription description, String[] arguments) {
        ChatgameContext chatgames = ChatgameContext.getContext(person);
        chatgames.toggleIgnoring();

        ConnectionContext.sendMessage(person, chatgames.isIgnoring() ? GameLanguage.getInstance().ignoring : GameLanguage.getInstance().noLongerIgnoring);
    }
}
