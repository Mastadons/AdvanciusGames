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
import net.advancius.placeholder.PlaceholderComponent;

@FlagManager.FlaggedClass
public class CooldownCommand implements CommandListener {

    @FlagManager.FlaggedMethod(priority = 5, flag = DefinedFlag.GAMES_PLUGIN_LOAD)
    public static void command() {
        AdvanciusBungee.getInstance().getCommandManager().registerListener(new CooldownCommand());
    }

    @CommandHandler(description = "chatgames.cooldown")
    public void onCommand(Person person, CommandDescription description, String[] arguments) throws Exception {
        final GameManager GAME_MANAGER = AdvanciusGames.getInstance().getGameManager();

        if (GAME_MANAGER.getCooldown() == -1) throw new Exception(GameLanguage.getInstance().activeGame);

        PlaceholderComponent component = new PlaceholderComponent(GameLanguage.getInstance().cooldown);
        component.replace("cooldown", GAME_MANAGER.getCooldown());
        component.translateColor();
        component.send(person);
    }
}
