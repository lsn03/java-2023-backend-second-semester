package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processor.UserState;
import edu.java.bot.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CancelCommand extends AbstractCommand {
    public static final String CANCEL_INPUT = "Ввод отменён.";
    public static final String NOTHING_TO_CANCEL = "Нечего отменять.";
    public static final String USER_NOT_REGISTERED =
        "Вы не зарегистрированы. Функционал бота не доступен. Введите /start для регистрации.";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Storage storage;

    @Autowired
    public CancelCommand(Storage storage) {
        super("/cancel");
        this.storage = storage;
    }

    @Override
    public String command() {
        return super.command;
    }

    @Override
    public String description() {
        return "Отменяет ввод URL в командах /track и /untrack";
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
        if (!storage.isUserAuth(chatId)) {
            return new SendMessage(chatId, USER_NOT_REGISTERED);
        } else {
            UserState state = storage.getUserState(chatId);
            return switch (state) {
                case AWAITING_URL_FOR_UN_TRACK, AWAITING_URL_FOR_TRACK -> processCancel(chatId);
                default -> new SendMessage(chatId, NOTHING_TO_CANCEL);
            };
        }

    }

    private SendMessage processCancel(Long chatId) {
        storage.setUserState(chatId,UserState.DEFAULT);
        return new SendMessage(chatId, CANCEL_INPUT);
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
