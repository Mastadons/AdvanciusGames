package net.advancius.game.question.equation;

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
public class EquationQuestionProvider implements QuestionProvider<EquationQuestion> {

    private final String fancyName = "Equation";
    private final String name = "equation";

    private final EquationConfiguration configuration;

    public static EquationQuestionProvider createProviderInstance() {
        return new EquationQuestionProvider(GameConfiguration.getInstance().equation);
    }

    @Override
    public EquationQuestion generateQuestion(CommandFlagss arguments) {
        int level = getQuestionLevel(arguments);

        for (int attempts=0; attempts < configuration.maximumGenerations; attempts++) {
            try {
                String expression = EquationGenerator.createExpression(level, level * configuration.numberRange);
                BigDecimal answer = Evaluator.evaluate(expression, configuration.divisionLimit);

                if (answer.abs().compareTo(BigDecimal.valueOf(configuration.maximumSolution)) == 1) continue;
                if (answer.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) continue;

                return new EquationQuestion(Evaluator.quickfix(expression), answer);
            } catch (UnsupportedOperationException exception) {}
        }
        throw new QuestionException("Ran out of generation attempts.", null);
    }

    private int getQuestionLevel(CommandFlags arguments) {
        return arguments.hasFlag("level") ? arguments.getFlag("level").asInteger() : configuration.defaultLevel;
    }

    @Override
    public void onQuestionSummoned(EquationQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().equation.questionSummoned);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
    }

    @Override
    public void onQuestionFinished(EquationQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().equation.questionFinished);
        placeholderComponent.replace("question", question);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
    }

    @Override
    public boolean onQuestionRightAnswer(Person person, EquationQuestion question) {
        PlaceholderComponent placeholderComponent = new PlaceholderComponent(GameLanguage.getInstance().equation.questionRightAnswer);
        placeholderComponent.replace("question", question);
        placeholderComponent.replace("person", person);
        placeholderComponent.translateColor();
        AdvanciusGames.getInstance().getGameManager().broadcastMessage(placeholderComponent.toTextComponentUnsafe());
        return false;
    }

    @Override
    public boolean onQuestionWrongAnswer(Person person, EquationQuestion question, String answer) {
        return false;
    }
}
