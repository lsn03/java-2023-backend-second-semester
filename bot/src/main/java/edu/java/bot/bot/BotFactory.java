package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public final class BotFactory {
    private BotFactory() {

    }

    public TelegramBot create(String token) {

        return new TelegramBot(token);
    }
}
