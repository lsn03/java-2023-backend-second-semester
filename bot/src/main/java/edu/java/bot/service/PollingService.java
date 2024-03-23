package edu.java.bot.service;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.bot.BotService;
import edu.java.bot.processor.UserMessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PollingService {
    private final BotService bot;
    private final UserMessageProcessor userMessageProcessor;

    @Autowired
    public PollingService(BotService bot, UserMessageProcessor userMessageProcessor) {
        this.bot = bot;
        this.userMessageProcessor = userMessageProcessor;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startPolling() {
        log.info("Start polling?");
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                var message = userMessageProcessor.process(update);
                if (message != null) {
                    bot.myExecute(message);
                }

            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }
}
