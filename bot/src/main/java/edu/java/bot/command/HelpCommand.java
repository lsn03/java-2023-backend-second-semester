package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {

        return "Show help";
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
        return new SendMessage(chatId, "Бот позволяет отслеживать обновления сайтов." + System.lineSeparator() +
            "Поддерживаются сайты: StackOverFlow, Github. Примеры корректных ссылок:" + System.lineSeparator() +
            "1. https://github.com/lsn03" + System.lineSeparator() +
            "2. https://stackoverflow.com/questions/6402162/how-to-enable-intellij-hot-code-swap" +
            System.lineSeparator() + "Примеры некорректных ссылок:" +System.lineSeparator()+
            "1. github.com/lsn03" + System.lineSeparator() +
            "2. stackoverflow.com/questions/6402162/how-to-enable-intellij-hot-code-swap" + System.lineSeparator()+
            "Поддерживаемые комманды доступны в меню"
        );

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
