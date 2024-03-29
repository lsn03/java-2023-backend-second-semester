package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.storage.Storage;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
        String username = update.message().chat().username();
        String text = update.message().text();
        logger.info("User @{} entered \"{}\" user_id={}", username, text, chatId);

        if (!storage.isUserAuth(chatId)) {
            return new SendMessage(chatId, CommandUtils.USER_NOT_REGISTERED);
        }
        var list = storage.getUserTracks(chatId);
        if (!list.isEmpty()) {
            return processGetList(list, chatId);
        } else {
            return new SendMessage(chatId, CommandUtils.NOTHING_TO_TRACK);
        }

    }

    @Override
    public boolean supports(Update update) {
        return Command.super.supports(update);
    }

    @Override
    public BotCommand toApiCommand() {
        return Command.super.toApiCommand();
    }

    private SendMessage processGetList(List<String> list, Long chatId) {
        st.setLength(0);
        st.append("Отслеживаемые ссылки:").append(System.lineSeparator());
        for (int i = 0; i < list.size(); i++) {
            st.append(i + 1).append(". ").append(list.get(i));
            if (i < list.size() - 1) {
                st.append(System.lineSeparator());
            }
        }

        return new SendMessage(chatId, st.toString());
    }
}
