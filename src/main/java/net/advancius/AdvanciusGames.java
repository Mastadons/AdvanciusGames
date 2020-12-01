package net.advancius;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import net.advancius.flag.DefinedFlag;
import net.advancius.flag.FlagManager;
import net.advancius.game.GameManager;
import net.md_5.bungee.api.plugin.Plugin;

public class AdvanciusGames extends Plugin {

    public static Gson GSON = new Gson();

    @Getter private static AdvanciusGames instance;

    @Getter @Setter private GameManager gameManager;

    @Override
    public void onEnable() {
        super.onEnable();
        AdvanciusGames.instance = this;

        FlagManager.runFlaggedMethods("net.advancius", DefinedFlag.GAMES_PLUGIN_LOAD);
    }

    @Override
    public void onDisable() {
        FlagManager.runFlaggedMethods("net.advancius", DefinedFlag.GAMES_PLUGIN_SAVE);

        super.onDisable();
        AdvanciusGames.instance = null;
    }
}
