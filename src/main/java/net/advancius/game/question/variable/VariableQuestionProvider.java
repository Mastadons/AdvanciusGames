package net.advancius.game.question.variable;

import lombok.Data;
import net.advancius.AdvanciusGames;
import net.advancius.command.CommandFlags;
import net.advancius.game.GameConfiguration;
import net.advancius.game.GameLanguage;
import net.advancius.game.commons.EquationGenerator;
import net.advancius.game.commons.Evaluator;
import net.advancius.game.question.QuestionException;
import net.advancius.game.question.QuestionProvider;
import net.advancius.person.Person;
import net.advancius.placeholder.PlaceholderComponent;

import java.math.BigDecimal;

@Data
public class VariableQuestionProvider implements QuestionProvider<VariableQuestion> {

    private final String fancyName = "Solve for Variable";
    private final String name = "variable";

    private final VariableConfiguration configuration;

    public static VariableQuestionProvider createProviderInstance() {
        return new VariableQuestionProvider(GameConfiguration.getInstance().variable);
    }

    @Override
    public VariableQuestion generateQuestion(CommandFlags arguments) {
        int minimum0 = arguments.hasFlag("minimum") ? arguments.getFlag("minimum").asInteger() : configuration.defaultMinimum;
        int maximum0 = arguments.hasFlag("maximum") ? arguments.getFlag("maximum").asInteger() : configuration.defaultMaximum;

        int level = getQuestionLevel(arguments);

        for (int attempts=0; attempts < configuration.maximumGenerations; attempts++) {
            try {
                int variableAnswer = generateNumber(Math.min(minimum0, maximum0), Math.max(minimum0, maximum0));
                String expression = EquationGenerator.createVariableExpression(level, level*configuration.numberRange, configuration.variableCharacter);
                BigDecimal answer = Evaluator.evaluateX(expression, configuration.variableCharacter, variableAnswer, configuration.divisionLimit);

                if (answer.equals(Evaluator.evaluateX(expression, configuration.variableCharacter, variableAnswer+1, configuration.divisionLimit))) continue;
                if (answer.abs().compareTo(BigDecimal.valueOf(configuration.maximumSolution)) == 1) continue;
                if (countOccurrences(expression, configuration.variableCharacter) != 1) continue;
                if (answer.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) continue;

                return new VariableQuestion(Evaluator.quickfix(expression), answer, variableAnswer);
            } catch (UnsupportedOperationException exception) {}
        }
        throw new QuestionException("Ran out of generation attempts.", null);
    }

    private int getQuestionLevel(CommandFlags arguments) {
        return arguments.hasFlag("level") ? arguments.getFlag("level").asInteger() : configuration.defaultLevel;
    }

    private int generateNumber(int minimum, int maximum) {
        return (int) ((Math.random() * (maximum - minimum)) + minimum);
    }

    private int countOccurrences(String string, char character) {
        int occurrences = 0;
        for (char character0 : string.toCharArray()) if (character0 == character) occurrences++;
        return occurrences;
    }

    @Override
    public void onQuestionSummoned(VariableQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().variable.questionSummoned);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
    }

    @Override
    public void onQuestionFinished(VariableQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().variable.questionFinished);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
    }

    @Override
    public boolean onQuestionRightAnswer(Person person, VariableQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().variable.questionRightAnswer);
        placeholderComponent.replace("question", question);
        placeholderComponent.replace("person", person);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
        return false;
    }

    @Override
    public boolean onQuestionWrongAnswer(Person person, VariableQuestion question, String answer) {
        return false;
    }
}
