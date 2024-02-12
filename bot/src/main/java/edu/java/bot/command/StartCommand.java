package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StartCommand extends AbstractCommand {

    public static final String ALREADY_AUTH = "Вы уже авторизованы.";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public StartCommand() {
        super("/start");
    }

    @Override
    public String command() {
        return super.command;
    }

    @Override
    public String description() {
        return "Регистрация в боте";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        logger.info(
            "User @{} entered \"{}\" user_id={}",
            update.message().chat().username(),
            update.message().text(),
            chatId
        );
        return new SendMessage(update.message().chat().id(), ALREADY_AUTH);
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
