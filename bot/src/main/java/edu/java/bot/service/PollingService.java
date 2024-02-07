package edu.java.bot.service;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.bot.BotService;
import edu.java.bot.processor.UserMessageProcessor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PollingService {
    private final BotService bot;
    private final UserMessageProcessor userMessageProcessor;

    @Autowired
    public PollingService(BotService bot, UserMessageProcessor userMessageProcessor) {
        this.bot = bot;
        this.userMessageProcessor = userMessageProcessor;
    }
    @PostConstruct
    public void init() {
        startPolling();
    }
    private void startPolling() {
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                userMessageProcessor.process(update);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
