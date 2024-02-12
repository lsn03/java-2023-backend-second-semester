package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exception.UnsupportedSiteException;
import edu.java.bot.parser.ResourceHandler;
import edu.java.bot.processor.UserState;
import edu.java.bot.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TrackCommand extends AbstractCommand {
    public static final String AWAITING_URL = "Ожидается ввод URL. Для отмены используйте /cancel.";
    public static final String URL_SUCCESSFULLY_ADDED = "URL добавлен в список отслеживаемых.";
    public static final String URL_ALREADY_EXIST = "Не удалось добавить URL. Ссылка уже отслеживается.";
    public static final String EXCEPTION_MESSAGE = " Попробуйте другой URL или используйте /cancel для отмены.";
    public static final String USER_NOT_REGISTERED =
        "Вы не зарегистрированы. Функционал бота не доступен. Введите /start для регистрации.";
    public static final String INPUT_URL_FOR_TRACK = "Пожалуйста, отправьте URL для отслеживания.";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Storage storage;

    @Autowired
    public TrackCommand(Storage storage) {
        super("/track");
        this.storage = storage;

    }

    @Override
    public String command() {
        return super.command;
    }

    @Override
    public String description() {
        return "Начать отслеживание ссылки";
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
        if (!storage.isUserAuth(chatId)) {
            return new SendMessage(chatId,USER_NOT_REGISTERED);
        }
        UserState state = storage.getUserState(chatId);
        return switch (state) {
            case AWAITING_URL_FOR_TRACK -> processAwaitingUrlState(chatId, text);
            case UNAUTHORIZED -> new SendMessage(chatId, USER_NOT_REGISTERED);
            case DEFAULT -> processDefaultState(chatId, text);
            default -> throw new IllegalArgumentException();
        };

    }

    private SendMessage processDefaultState(Long chatId, String text) {

        if (text.equals("/track")) {
            storage.setUserState(chatId, UserState.AWAITING_URL_FOR_TRACK);
            return new SendMessage(chatId, INPUT_URL_FOR_TRACK);
        } else {
            throw new IllegalArgumentException();
        }

    }

    private SendMessage processAwaitingUrlState(Long chatId, String text) {
        if (!text.startsWith("/")) {
            SendMessage message;
            try {
                var result = storage.addUrl(chatId, text);
                if (result) {
                    storage.setUserState(chatId, UserState.DEFAULT);
                    message = new SendMessage(chatId, URL_SUCCESSFULLY_ADDED);
                } else {
                    message = new SendMessage(chatId, URL_ALREADY_EXIST);
                }
                return message;
            } catch (UnsupportedSiteException e) {
                return new SendMessage(chatId,e.getMessage()+EXCEPTION_MESSAGE);
            }

        } else {
            return new SendMessage(chatId, AWAITING_URL);
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
