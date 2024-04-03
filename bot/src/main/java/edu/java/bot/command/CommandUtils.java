package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;

public final class CommandUtils {
    public static final String CANCEL_INPUT = "Ввод отменён.";
    public static final String NOTHING_TO_CANCEL = "Нечего отменять.";
    public static final String NOTHING_TO_TRACK = "Нет ссылок для отслеживания. Введите /track.";
    public static final String USER_NOT_REGISTERED =
        "Вы не зарегистрированы. Функционал бота не доступен. Введите /start для регистрации.";
    public static final String ALREADY_AUTH = "Вы уже авторизованы.";
    public static final String USER_REGISTERED_SUCCESS =
        "Вы зарегистрированы. Функционал бота доступен. Используйте /help для списка команд.";
    public static final String AWAITING_URL = "Ожидается ввод URL. Для отмены используйте /cancel.";
    public static final String URL_SUCCESSFULLY_ADDED = "URL добавлен в список отслеживаемых.";
    public static final String URL_ALREADY_EXIST = "Не удалось добавить URL. Ссылка уже отслеживается.";
    public static final String EXCEPTION_MESSAGE = " Попробуйте другой URL или используйте /cancel для отмены.";
    public static final String INPUT_URL_FOR_TRACK = "Пожалуйста, отправьте URL для отслеживания.";
    public static final String UNKNOWN_COMMAND_HELP = "Неизвестная команда. Используйте /help для списка команд.";

    public static final String INPUT_URL_FOR_UN_TRACK = "Пожалуйста, отправьте URL для прекращения отслеживания.";
    public static final String URL_SUCCESSFULLY_REMOVED = "URL удалён из списка отслеживаемых.";
    public static final String URL_NOT_FOUND = "URL не найден. Не удалось удалить URL.";


    private CommandUtils() {

    }
    public static void extractMessageForLog(Update update, Logger log) {
        Long chatId = update.message().chat().id();
        String username = update.message().chat().username();
        String text = update.message().text();
        String userFirstName = update.message().chat().firstName();
        log.info(
            "User @{} firstName={} entered \"{}\" user_id={}",
            username,
            userFirstName,
            text,
            chatId
        );
    }

}
