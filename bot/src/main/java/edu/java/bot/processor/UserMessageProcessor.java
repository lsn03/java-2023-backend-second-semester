package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.command.UnknownCommand;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMessageProcessor {
    private final Map<String, Command> commands;
    private final UnknownCommand unknownCommand;

    @Autowired
    public UserMessageProcessor(List<Command> commandList) {

        this.commands = commandList.stream()
            .collect(Collectors.toMap(Command::command, Function.identity()));
        unknownCommand = new UnknownCommand();
    }

    public SendMessage process(Update update) {
        String text = update.message().text();
        var result = commands.getOrDefault(text, null);
        SendMessage message = null;
        if (result != null) {
            message = result.handle(update);
        } else if (text.startsWith("/")) {
            message = unknownCommand.handle(update);
        } else {
            message = null;
        }
        return message;
    }
}
