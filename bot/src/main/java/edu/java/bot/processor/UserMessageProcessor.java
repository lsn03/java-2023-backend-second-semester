package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CancelCommand;
import edu.java.bot.command.Command;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UnknownCommand;
import edu.java.bot.exception.UnsupportedSiteException;
import edu.java.bot.service.CommandService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMessageProcessor {
    public static final String USER_REGISTERED_SUCCESS =
        "Вы зарегистрированы. Функционал бота доступен. Используйте /help для списка команд.";
    public static final String USER_NOT_REGISTERED =
        "Вы не зарегистрированы. Функционал бота не доступен. Введите /start для регистрации.";
    public static final String CANCEL_INPUT = "Ввод отменён.";
    public static final String AWAITING_URL = "Ожидается ввод URL. Для отмены используйте /cancel.";
    public static final String ERROR_TRY_AGAIN = "Произошла ошибка, попробуйте ещё раз.";
    public static final String EXCEPTION_MESSAGE = " Попробуйте другой URL или используйте /cancel для отмены.";
    public static final String URL_SUCCESSFULLY_REMOVED = "URL удалён из списка отслеживаемых.";
    public static final String URL_NOT_FOUND = "URL не найден. Не удалось удалить URL.";
    public static final String URL_SUCCESSFULLY_ADDED = "URL добавлен в список отслеживаемых.";
    public static final String URL_ALREADY_EXIST = "Не удалось добавить URL. Ссылка уже отслеживается.";
    public static final String NOTHING_TO_CANCEL = "Нечего отменять.";
    private final CommandService commandService;
    private final Map<String, Command> commands;
    private final UnknownCommand unknownCommand;
    private final Map<Long, UserState> userStates;

    @Autowired
    public UserMessageProcessor(List<Command> commandList, CommandService commandService) {

        this.commands = commandList.stream()
            .collect(Collectors.toMap(Command::command, Function.identity()));

        unknownCommand = new UnknownCommand();
        this.commandService = commandService;
        userStates = new HashMap<>();
    }

    public SendMessage process(Update update) {
        Long chatId = update.message().chat().id();
        String text = update.message().text();

        UserState state = userStates.getOrDefault(chatId, UserState.UNAUTHORIZED);

        return switch (state) {
            case UNAUTHORIZED -> processUnauthorized(chatId, text, update);
            case AWAITING_URL_FOR_TRACK, AWAITING_URL_FOR_UN_TRACK -> processAwaitingUrlState(chatId, text, state);
            default -> processDefaultState(chatId, text, update);
        };

    }

    private SendMessage processUnauthorized(Long chatId, String text, Update update) {
        UserState state = userStates.getOrDefault(chatId, UserState.UNAUTHORIZED);
        SendMessage message;
        if (text.equals("/start") && state == UserState.UNAUTHORIZED) {
            commandService.authorizeUser(chatId);
            userStates.put(chatId, UserState.DEFAULT);
            message = new SendMessage(
                chatId,
                USER_REGISTERED_SUCCESS
            );
        } else {
            message = new SendMessage(
                chatId,
                USER_NOT_REGISTERED
            );
        }
        return message;
    }

    private SendMessage processAwaitingUrlState(Long chatId, String text, UserState state) {
        SendMessage message;
        if (text.equals("/cancel")) {
            userStates.put(chatId, UserState.DEFAULT);
            message = new SendMessage(chatId, CANCEL_INPUT);
        } else if (!text.startsWith("/")) {
            message = processUrlInput(chatId, text, state);
        } else {
            message = new SendMessage(chatId, AWAITING_URL);
        }
        return message;
    }

    private SendMessage processUrlInput(Long chatId, String text, UserState state) {
        try {

            return switch (state) {
                case AWAITING_URL_FOR_TRACK -> processAddTrack(chatId, text);
                case AWAITING_URL_FOR_UN_TRACK -> processRemoveTrack(chatId, text);
                default -> new SendMessage(chatId, ERROR_TRY_AGAIN);
            };

        } catch (UnsupportedSiteException e) {
            return new SendMessage(
                chatId,
                e.getMessage() + EXCEPTION_MESSAGE
            );
        }
    }

    private SendMessage processRemoveTrack(Long chatId, String text) {
        SendMessage message;
        var result = commandService.removeTrack(chatId, text);
        if (result) {
            userStates.put(chatId, UserState.DEFAULT);
            message = new SendMessage(chatId, URL_SUCCESSFULLY_REMOVED);
        } else {
            message = new SendMessage(chatId, URL_NOT_FOUND);
        }
        return message;
    }

    private SendMessage processAddTrack(Long chatId, String text) {
        SendMessage message;
        var result = commandService.addTrack(chatId, text);
        if (result) {
            userStates.put(chatId, UserState.DEFAULT);
            message = new SendMessage(chatId, URL_SUCCESSFULLY_ADDED);
        } else {
            message = new SendMessage(chatId, URL_ALREADY_EXIST);
        }
        return message;
    }

    private SendMessage processDefaultState(Long chatId, String text, Update update) {
        Command command = commands.getOrDefault(text, unknownCommand);
        SendMessage message;
        if (command instanceof UnknownCommand && !text.startsWith("/")) {
            message = command.handle(update);
        } else if (command instanceof CancelCommand) {
            message = new SendMessage(chatId, NOTHING_TO_CANCEL);
        } else {
            if (text.equals("/track")) {
                userStates.put(chatId, UserState.AWAITING_URL_FOR_TRACK);
            } else if (text.equals("/untrack")) {
                userStates.put(chatId, UserState.AWAITING_URL_FOR_UN_TRACK);
            } else {
                userStates.put(chatId, UserState.DEFAULT);
            }
            message = command.handle(update);
        }
        return message;
    }
}
