package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnknownCommand implements Command {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public String command() {
        return null;
    }

    @Override
    public String description() {
        return "UnknownCommand";
    }

    @Override
    public SendMessage handle(Update update) {
        LOGGER.warn("User entered {}", description());
        return new SendMessage(update.message().chat().id(), "Команда неизвестна");
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
