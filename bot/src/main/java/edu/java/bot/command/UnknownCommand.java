package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnknownCommand extends AbstractCommand {
    public static final String UNKNOWN_COMMAND_HELP = "Неизвестная команда. Используйте /help для списка команд.";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UnknownCommand() {
        super("/");
    }

    @Override
    public String command() {
        return super.command;
    }

    @Override
    public String description() {
        return "Неизвестная комманда.";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        logger.warn(
            "User @{} entered \"{}\" user_id={}",
            update.message().chat().username(),
            update.message().text(),
            chatId
        );

        return new SendMessage(
            update.message().chat().id(),
            UNKNOWN_COMMAND_HELP
        );
    }

    @Override
    public boolean supports(Update update) {
        return super.supports(update);
    }

    @Override
    public BotCommand toApiCommand() {
        return super.toApiCommand();
    }
}
