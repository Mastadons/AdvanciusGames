package net.advancius.game.command;

import lombok.Data;
import lombok.Getter;
import net.advancius.AdvanciusBungee;
import net.advancius.AdvanciusLogger;
import net.advancius.command.CommandDescription;
import net.advancius.file.FileManager;
import net.advancius.flag.DefinedFlag;
import net.advancius.flag.FlagManager;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@FlagManager.FlaggedClass
@Data
public class CommandConfiguration {

    @Getter private static CommandConfiguration instance;

    @Getter private static Yaml configurationYaml;
    @Getter private static File configurationFile;

    @FlagManager.FlaggedMethod(flag = DefinedFlag.GAMES_PLUGIN_LOAD, priority = 0)
    public static void load() throws FileNotFoundException {
        AdvanciusLogger.info("Loading game command configuration...");

        configurationYaml = new Yaml(new CustomClassLoaderConstructor(CommandConfiguration.class.getClassLoader()));
        configurationYaml.setBeanAccess(BeanAccess.FIELD);
        configurationFile = FileManager.getServerFile("commands.yml", "commands.yml");
        FileReader configurationReader = new FileReader(configurationFile);

        instance = configurationYaml.loadAs(configurationReader, CommandConfiguration.class);

        for (CommandDescription commandDescription : CommandConfiguration.getInstance().getCommands()) {
            if (commandDescription == null) continue;
            AdvanciusBungee.getInstance().getCommandManager().addDescription(commandDescription);
        }

        AdvanciusLogger.info("Loaded game command configuration!");
    }

    private List<CommandDescription> commands;
}
