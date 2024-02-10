package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CancelCommand;
import edu.java.bot.command.Command;
import edu.java.bot.command.UnknownCommand;
import edu.java.bot.exception.UnsupportedSiteException;
import edu.java.bot.service.CommandService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMessageProcessor {
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
                "Вы зарегистрированы. Функционал бота доступен. Используйте /help для списка команд. "
            );
        } else {
            message = new SendMessage(
                chatId,
                "Вы не зарегистрированы. Функционал бота не доступен. Введите /start для регистрации."
            );
        }
        return message;
    }

    private SendMessage processAwaitingUrlState(Long chatId, String text, UserState state) {
        SendMessage message;
        if (text.equals("/cancel")) {
            userStates.put(chatId, UserState.DEFAULT);
            message = new SendMessage(chatId, "Ввод отменён.");
        } else if (!text.startsWith("/")) {
            message = processUrlInput(chatId, text, state);
        } else {
            message = new SendMessage(chatId, "Ожидается ввод URL. Для отмены используйте /cancel.");
        }
        return message;
    }

    private SendMessage processUrlInput(Long chatId, String text, UserState state) {
        try {
            SendMessage message;
            if (state == UserState.AWAITING_URL_FOR_TRACK) {
                message = processAddTrack(commandService.addTrack(chatId, text),
                    chatId,
                    "URL добавлен в список отслеживаемых.",
                    "Не удалось добавить URL. Ссылка уже отслеживается."
                );
            } else if (state == UserState.AWAITING_URL_FOR_UN_TRACK) {
                message = processAddTrack(commandService.removeTrack(chatId, text),
                    chatId,
                    "URL удалён из списка отслеживаемых.",
                    "URL не найден. Не удалось удалить URL."
                );
            } else {
                message = new SendMessage(chatId, "Произошла ошибка, попробуйте ещё раз.");
            }
            return message;
        } catch (UnsupportedSiteException e) {
            return new SendMessage(
                chatId,
                e.getMessage() + " Попробуйте другой URL или используйте /cancel для отмены."
            );
        }
    }

    @NotNull
    private SendMessage processAddTrack(
        boolean commandService,
        Long chatId,
        String text,
        String text1
    ) {
        SendMessage message;
        var result = commandService;
        if (result) {
            userStates.put(chatId, UserState.DEFAULT);
            message = new SendMessage(chatId, text);
        } else {
            message = new SendMessage(chatId, text1);
        }
        return message;
    }

    private SendMessage processDefaultState(Long chatId, String text, Update update) {
        Command command = commands.getOrDefault(text, unknownCommand);
        SendMessage message;
        if (command instanceof UnknownCommand && !text.startsWith("/")) {
            message = command.handle(update);
        } else if (command instanceof CancelCommand) {
            message = new SendMessage(chatId, "Нечего отменять.");
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
