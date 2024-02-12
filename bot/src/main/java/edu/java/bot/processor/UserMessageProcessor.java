package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CancelCommand;
import edu.java.bot.command.Command;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UnTrackCommand;
import edu.java.bot.command.UnknownCommand;
import edu.java.bot.exception.UnsupportedSiteException;
import edu.java.bot.service.CommandService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import edu.java.bot.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMessageProcessor {



    public static final String ERROR_TRY_AGAIN = "Произошла ошибка, попробуйте ещё раз.";


    private final Storage storage;
    private final Map<String, Command> commands;
    private final UnknownCommand unknownCommand;


    @Autowired
    public UserMessageProcessor(List<Command> commandList, CommandService commandService, Storage storage) {

        this.commands = commandList.stream()
            .collect(Collectors.toMap(Command::command, Function.identity()));
        this.storage = storage;

        unknownCommand = new UnknownCommand();


    }

    public SendMessage process(Update update) {
        Long chatId = update.message().chat().id();
        String text = update.message().text();

        UserState state = storage.getUserState(chatId);
        Command command = commands.getOrDefault(text, unknownCommand);
        if(command instanceof CancelCommand){
            return command.handle(update);
        }
        return switch (state) {
            case UNAUTHORIZED -> command.handle(update);
            case AWAITING_URL_FOR_TRACK -> commands.get("/track").handle(update);
            case AWAITING_URL_FOR_UN_TRACK ->commands.get("/untrack").handle(update);
            case DEFAULT -> command.handle(update);
            default -> command.handle(update);
        };

    }



}
