package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.InMemoryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final InMemoryStorage storage;

    @Autowired
    public TrackCommand(InMemoryStorage storage) {
        this.storage = storage;
    }

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
        Long chatId = update.message().chat().id();
        LOGGER.info(
            "User @{} entered \"{}\" user_id={}",
            update.message().chat().username(),
            update.message().text(),
            chatId
        );

        storage.setAwaitingUrl(chatId, true);
        return new SendMessage(chatId, "Пожалуйста, отправьте URL для отслеживания.");

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
