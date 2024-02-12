package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand extends AbstractCommand {

    public static final String ALREADY_AUTH = "Вы уже авторизованы.";
    public static final String USER_REGISTERED_SUCCESS =
        "Вы зарегистрированы. Функционал бота доступен. Используйте /help для списка команд.";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Storage storage;

    @Autowired
    public StartCommand(Storage storage) {
        super("/start");
        this.storage = storage;
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
        String username = update.message().chat().username();
        String text = update.message().text();
        logger.info(
            "User @{} entered \"{}\" user_id={}",
            username,
            text,
            chatId
        );
        if (storage.isUserAuth(chatId)) {
            logger.info("User @{} id={} already sign in", username,chatId);
            return new SendMessage(chatId, ALREADY_AUTH);
        } else {
            storage.authUser(chatId);
            logger.info("User @{} id={} was successfully registered", username,chatId);
            return new SendMessage(chatId, USER_REGISTERED_SUCCESS);
        }

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
