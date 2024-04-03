package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exception.IncorrectParametersException;
import edu.java.bot.exception.ListEmptyException;
import edu.java.bot.exception.RepeatTrackException;
import edu.java.bot.model.dto.response.LinkResponse;
import edu.java.bot.storage.Storage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ListCommand implements Command {


    private final Storage storage;
    private final StringBuilder st;

    public ListCommand(Storage storage) {

        this.storage = storage;
        st = new StringBuilder();
    }

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Список отслеживаемых сайтов";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        CommandUtils.extractMessageForLog(update, log);

        if (!storage.isUserAuth(chatId)) {
            return new SendMessage(chatId, CommandUtils.USER_NOT_REGISTERED);
        }
        List<LinkResponse> list = null;
        SendMessage message = null;
        try {
            list = storage.getUserTracks(chatId);
        } catch (IncorrectParametersException e) {

            if (e.getMessage().contains(RepeatTrackException.class.getSimpleName())) {
                message = new SendMessage(chatId, CommandUtils.URL_ALREADY_EXIST);
            } else if (e.getMessage().contains(ListEmptyException.class.getSimpleName())) {
                message = new SendMessage(chatId, CommandUtils.NOTHING_TO_TRACK);
            }
            return message;
        }
        if (!list.isEmpty()) {
            message = processGetList(list, chatId);
        } else {
            message = new SendMessage(chatId, CommandUtils.NOTHING_TO_TRACK);
        }
        return message;

    }

    @Override
    public boolean supports(Update update) {
        return Command.super.supports(update);
    }

    @Override
    public BotCommand toApiCommand() {
        return Command.super.toApiCommand();
    }

    private SendMessage processGetList(List<LinkResponse> list, Long chatId) {
        st.setLength(0);
        st.append("Отслеживаемые ссылки:").append(System.lineSeparator());
        for (int i = 0; i < list.size(); i++) {
            st.append(i + 1).append(". ").append(list.get(i).getUrl());
            if (i < list.size() - 1) {
                st.append(System.lineSeparator());
            }
        }

        return new SendMessage(chatId, st.toString());
    }
}
