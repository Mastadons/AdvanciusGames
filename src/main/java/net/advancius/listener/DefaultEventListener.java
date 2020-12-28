package net.advancius.listener;

import net.advancius.AdvanciusBungee;
import net.advancius.AdvanciusGames;
import net.advancius.channel.message.event.MessageGenerateEvent;
import net.advancius.event.EventHandler;
import net.advancius.event.EventListener;
import net.advancius.flag.DefinedFlag;
import net.advancius.flag.FlagManager;
import net.advancius.game.GameConfiguration;
import net.advancius.game.GameManager;
import net.advancius.game.context.ChatgameContext;
import net.advancius.game.question.Question;
import net.advancius.game.question.QuestionProvider;
import net.advancius.game.question.SummonedQuestion;
import net.advancius.person.context.ContextManager;
import net.advancius.person.event.PersonJoinEvent;
import net.advancius.person.event.PersonLoadEvent;

import java.util.concurrent.TimeUnit;

@FlagManager.FlaggedClass
public class DefaultEventListener implements EventListener {

    @FlagManager.FlaggedMethod(flag = DefinedFlag.GAMES_PLUGIN_LOAD, priority = 550)
    private static void eventListener() {
        AdvanciusBungee.getInstance().getEventManager().registerListener(new DefaultEventListener());
    }

    @EventHandler(Integer.MIN_VALUE)
    public void onPersonLoad(PersonLoadEvent event) {
        ContextManager contextManager = event.getPerson().getContextManager();

        contextManager.addContext(ChatgameContext.class, 10);
    }

    @EventHandler(Integer.MAX_VALUE)
    public void onMessagePreSend(MessageGenerateEvent event) {
        GameManager gameManager = AdvanciusGames.getInstance().getGameManager();
        if (gameManager.getQuestion() == null) return;

        SummonedQuestion summonedQuestion = gameManager.getQuestion();
        if (summonedQuestion.isAnswered()) return;

        boolean correctAnswer = summonedQuestion.getQuestion().isAnswer(event.getMessage());
        ChatgameContext.incrementScore(event.getSender(), summonedQuestion.getQuestionProvider(), correctAnswer);
        if (correctAnswer) {
            summonedQuestion.setAnswered(true);
            summonedQuestion.getQuestionProvider().onQuestionRightAnswer(event.getSender(), summonedQuestion.getQuestion());
            gameManager.sendRewards(event.getSender());
            gameManager.setDuration(0);
            event.setCancelled(true);
        }
        else if (summonedQuestion.getQuestionProvider().onQuestionWrongAnswer(event.getSender(), summonedQuestion.getQuestion(), event.getMessage())) {
            event.setCancelled(true);
        }
    }
}
