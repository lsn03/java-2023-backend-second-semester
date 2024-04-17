package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CancelCommand;
import edu.java.bot.command.Command;
import edu.java.bot.command.UnknownCommand;
import edu.java.bot.storage.Storage;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMessageProcessor {
    private final Storage storage;
    private final Map<String, Command> commands;
    private final UnknownCommand unknownCommand;

    @Autowired
    public UserMessageProcessor(List<Command> commandList, Storage storage) {

        this.commands = commandList.stream()
            .collect(Collectors.toMap(Command::command, Function.identity()));
        this.storage = storage;

        unknownCommand = new UnknownCommand();

    }

    public SendMessage process(Update update) {
        if (update.message() == null) {
            return null;
        }
        Long chatId = update.message().chat().id();
        String text = update.message().text();

        UserState state = storage.getUserState(chatId);

        Command command = commands.getOrDefault(text, unknownCommand);
        if (command instanceof CancelCommand) {
            return command.handle(update);
        }
        return switch (state) {
            case UNAUTHORIZED -> command.handle(update);
            case AWAITING_URL_FOR_TRACK -> commands.get("/track").handle(update);
            case AWAITING_URL_FOR_UN_TRACK -> commands.get("/untrack").handle(update);
            case DEFAULT -> command.handle(update);
        };

    }

}
