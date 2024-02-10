package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.command.UnknownCommand;
import edu.java.bot.exception.UnsupportedSiteException;
import edu.java.bot.service.CommandService;
import edu.java.bot.service.InMemoryStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMessageProcessor {
    private final CommandService commandService;
    private final InMemoryStorage storage;
    private final Map<String, Command> commands;
    private final UnknownCommand unknownCommand;
    private Map<Long, Command> prevUserCommand;

    @Autowired
    public UserMessageProcessor(List<Command> commandList, CommandService commandService, InMemoryStorage storage) {

        this.commands = commandList.stream()
            .collect(Collectors.toMap(Command::command, Function.identity()));
//        commands.remove("/cancel");

        unknownCommand = new UnknownCommand();
        this.commandService = commandService;
        this.storage = storage;
        prevUserCommand = new HashMap<>();
    }

    public SendMessage process(Update update) {
        Long chatId = update.message().chat().id();
        String text = update.message().text();

        if (storage.isAwaitingUrl(chatId)) {
            if (text.equals("/cancel")) {
                storage.setAwaitingUrl(chatId, false);
                return new SendMessage(chatId, "Запрос на отмену ввода отправлен.");
            } else if (!text.startsWith("/")) {
                try {
                    return processAwaitingUrl(chatId, text);
                } catch (UnsupportedSiteException e) {
                    return new SendMessage(
                        chatId,
                        e.getMessage() + ". Напишите /cancel для отмены ввода или введите корректную ссылку"
                    );
                }
            }
        }

        var result = commands.getOrDefault(text, null);
        SendMessage message;
        if (result != null) {
            message = result.handle(update);
            prevUserCommand.put(chatId, result);
        } else if (text.startsWith("/")) {
            message = unknownCommand.handle(update);
        } else {
            message = new SendMessage(chatId, "Запрос отправлен.");
        }
        return message;
    }

    private SendMessage processAwaitingUrl(Long chatId, String text) {
        var prevCommand = prevUserCommand.get(chatId);
        SendMessage sendMessage = null;

        try {
            if (prevCommand.command().equals("/track")) {
                commandService.addTrack(chatId, text);

                sendMessage = new SendMessage(chatId, "Ссылка добавлена к отслеживанию.");
            } else if (prevCommand.command().equals("/untrack")) {
                commandService.removeTrack(chatId, text);

                sendMessage = new SendMessage(chatId, "Ссылка убрана из списка отслеживаемых к отслеживанию.");
            }
            storage.setAwaitingUrl(chatId, false);
        } catch (UnsupportedSiteException e) {
            throw new UnsupportedSiteException(e.getMessage());
        }
        return sendMessage;
    }
}
