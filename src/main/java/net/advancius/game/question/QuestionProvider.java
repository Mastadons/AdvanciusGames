package net.advancius.game.question;

import net.advancius.command.CommandFlags;
import net.advancius.person.Person;

public interface QuestionProvider<T extends Question> {

    T generateQuestion(CommandFlags arguments);

    void onQuestionSummoned(T question);
    void onQuestionFinished(T question);

    boolean onQuestionRightAnswer(Person person, T question);
    boolean onQuestionWrongAnswer(Person person, T question, String answer);

    String getName();
    String getFancyName();
}
