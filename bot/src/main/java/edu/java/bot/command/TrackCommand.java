package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Track command";
    }

    @Override
    public SendMessage handle(Update update) {
        LOGGER.info(
            "User entered {} user id {} user name {}",
            update.message().text(),
            update.message().chat().id(),
            update.message().chat().username()
        );
        return new SendMessage(update.message().chat().id(),"Вызвана команда "+command());
    }

    @Override
    public boolean supports(Update update) {
        return Command.super.supports(update);
    }

    @Override
    public BotCommand toApiCommand() {
        return Command.super.toApiCommand();
    }
}
