package net.advancius.game;

import lombok.Data;
import lombok.Getter;
import net.advancius.AdvanciusLogger;
import net.advancius.command.CommandDescription;
import net.advancius.file.FileManager;
import net.advancius.flag.DefinedFlag;
import net.advancius.flag.FlagManager;
import net.advancius.game.question.equation.EquationConfiguration;
import net.advancius.game.question.number.NumberConfiguration;
import net.advancius.game.question.quicktype.QuicktypeConfiguration;
import net.advancius.game.question.scramble.ScrambleConfiguration;
import net.advancius.game.question.variable.VariableConfiguration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Data
@FlagManager.FlaggedClass
public class GameConfiguration {

    @Getter
    private static GameConfiguration instance;

    @Getter private static Yaml configurationYaml;
    @Getter private static File configurationFile;

    @FlagManager.FlaggedMethod(flag = DefinedFlag.GAMES_PLUGIN_LOAD)
    public static void load() throws FileNotFoundException {
        AdvanciusLogger.info("Loading game configuration...");

        configurationYaml = new Yaml(new CustomClassLoaderConstructor(GameConfiguration.class.getClassLoader()));
        configurationYaml.setBeanAccess(BeanAccess.FIELD);
        configurationFile = FileManager.getServerFile("configuration.yml", "configuration.yml");
        FileReader configurationReader = new FileReader(configurationFile);

        instance = configurationYaml.loadAs(configurationReader, GameConfiguration.class);
        AdvanciusLogger.info("Loaded game configuration!");
    }

    public String bungeeRewardCommand;
    public String spigotRewardCommand;

    public int minimumPlayers;
    public int maximumPlayers;

    public int firstSummonDelay;
    public int duration;
    public int cooldown;

    public EquationConfiguration equation;
    public NumberConfiguration number;
    public QuicktypeConfiguration quicktype;
    public ScrambleConfiguration scramble;
    public VariableConfiguration variable;

    public CommandDescription commandDescription;
}
