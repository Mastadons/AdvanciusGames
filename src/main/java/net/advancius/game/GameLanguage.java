package net.advancius.game;

import lombok.Data;
import lombok.Getter;
import net.advancius.AdvanciusLogger;
import net.advancius.file.FileManager;
import net.advancius.flag.DefinedFlag;
import net.advancius.flag.FlagManager;
import net.advancius.game.question.QuestionLanguage;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Data
@FlagManager.FlaggedClass
public class GameLanguage {

    @Getter
    private static GameLanguage instance;

    @Getter private static Yaml configurationYaml;
    @Getter private static File configurationFile;

    @FlagManager.FlaggedMethod(flag = DefinedFlag.GAMES_PLUGIN_LOAD)
    public static void load() throws FileNotFoundException {
        AdvanciusLogger.info("Loading game language...");

        configurationYaml = new Yaml(new CustomClassLoaderConstructor(GameLanguage.class.getClassLoader()));
        configurationYaml.setBeanAccess(BeanAccess.FIELD);
        configurationFile = FileManager.getServerFile("language.yml", "language.yml");
        FileReader configurationReader = new FileReader(configurationFile);

        instance = configurationYaml.loadAs(configurationReader, GameLanguage.class);
        AdvanciusLogger.info("Loaded game language!");
    }

    public QuestionLanguage country, equation, hangman, number, quicktype, scramble, trivia, variable;

    public String info;
    public String reload;
}
