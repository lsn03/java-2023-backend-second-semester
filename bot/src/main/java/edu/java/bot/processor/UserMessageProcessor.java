package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UnTrackCommand;
import edu.java.bot.command.UnknownCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserMessageProcessor {
    private Map<String,Command> commands;
    private UnknownCommand unknownCommand = new UnknownCommand();
    @Autowired
    public UserMessageProcessor(List<Command> commandList) {

        this.commands = commandList.stream()
            .collect(Collectors.toMap(Command::command, Function.identity()));

    }

    public SendMessage process(Update update) {
        String text = update.message().text();
        var result = commands.getOrDefault(text,null);
        if(result!=null){
            result.handle(update);
        }
        else{
            unknownCommand.handle(update);
        }
        return null;
    }
}
