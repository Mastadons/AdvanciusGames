package net.advancius.game.commons;

import lombok.Data;
import net.advancius.file.FileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Data
public class WordList {

    private final List<String> wordList;

    public static WordList generateList(String serverFilepath, String sourceFilepath) throws FileNotFoundException {
        File countriesFile = FileManager.getServerFile(serverFilepath, sourceFilepath);

        List<String> wordList = new ArrayList<>();

        Scanner scanner = new Scanner(new FileReader(countriesFile));
        while (scanner.hasNextLine()) wordList.add(scanner.nextLine());
        scanner.close();

        return new WordList(wordList);
    }

    public String getRandom() {
        return wordList.get((int)(Math.random() * wordList.size()));
    }
}
