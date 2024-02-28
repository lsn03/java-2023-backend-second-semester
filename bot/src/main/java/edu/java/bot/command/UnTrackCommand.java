package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exception.UnsupportedSiteException;
import edu.java.bot.processor.UserState;
import edu.java.bot.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UnTrackCommand implements Command {
    public static final String USER_NOT_REGISTERED =
        "Вы не зарегистрированы. Функционал бота не доступен. Введите /start для регистрации.";
    public static final String AWAITING_URL = "Ожидается ввод URL. Для отмены используйте /cancel.";
    public static final String INPUT_URL_FOR_UN_TRACK = "Пожалуйста, отправьте URL для прекращения отслеживания.";
    public static final String URL_SUCCESSFULLY_REMOVED = "URL удалён из списка отслеживаемых.";
    public static final String URL_NOT_FOUND = "URL не найден. Не удалось удалить URL.";
    public static final String EXCEPTION_MESSAGE = " Попробуйте другой URL или используйте /cancel для отмены.";

    private final Storage storage;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Прекратить отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String username = update.message().chat().username();
        String text = update.message().text();
        log.info(
            "User @{} entered \"{}\" user_id={}",
            username,
            text,
            chatId
        );
        if (!storage.isUserAuth(chatId)) {
            return new SendMessage(chatId, USER_NOT_REGISTERED);
        }
        UserState state = storage.getUserState(chatId);

        return switch (state) {
            case UNAUTHORIZED -> new SendMessage(chatId, USER_NOT_REGISTERED);
            case AWAITING_URL_FOR_UN_TRACK -> processUnTrackUrl(chatId, text);
            case DEFAULT -> processDefaultUnTrack(chatId);
            default -> new SendMessage(chatId, EXCEPTION_MESSAGE);
        };

    }

    @Override
    public boolean supports(Update update) {
        return Command.super.supports(update);
    }

    @Override
    public BotCommand toApiCommand() {
        return Command.super.toApiCommand();
    }

    private SendMessage processDefaultUnTrack(Long chatId) {
        storage.setUserState(chatId, UserState.AWAITING_URL_FOR_UN_TRACK);

        return new SendMessage(chatId, INPUT_URL_FOR_UN_TRACK);
    }

    private SendMessage processUnTrackUrl(Long chatId, String text) {

        if (text.startsWith("/")) {
            return new SendMessage(chatId, AWAITING_URL);
        }

        try {
            var result = storage.removeUrl(chatId, text);
            if (result) {
                storage.setUserState(chatId, UserState.DEFAULT);
                return new SendMessage(chatId, URL_SUCCESSFULLY_REMOVED);
            } else {
                return new SendMessage(chatId, URL_NOT_FOUND);
            }
        } catch (UnsupportedSiteException e) {
            return new SendMessage(chatId, e.getMessage() + EXCEPTION_MESSAGE);
        }
    }
}
