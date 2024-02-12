package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand extends AbstractCommand {
    public static final String NOTHING_TO_TRACK = "Нет ссылок для отслеживания. Введите /track.";
    public static final String USER_NOT_REGISTERED =
        "Вы не зарегистрированы. Функционал бота не доступен. Введите /start для регистрации.";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Storage storage;

    @Autowired
    public ListCommand(Storage storage) {
        super("/list");
        this.storage = storage;
    }

    @Override
    public String command() {
        return super.command;
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
        logger.info(
            "User @{} entered \"{}\" user_id={}",
            username,
            text,
            chatId
        );

        if (!storage.isUserAuth(chatId)) {
            return new SendMessage(chatId,USER_NOT_REGISTERED);
        }
        var list = storage.getUserTracks(chatId);
        if (!list.isEmpty()) {
            StringBuilder st = new StringBuilder();
            st.append("Отслеживаемые ссылки:").append(System.lineSeparator());
            for (int i = 0; i < list.size(); i++) {
                st.append(i + 1).append(". ").append(list.get(i));
                if (i < list.size() - 1) {
                    st.append(System.lineSeparator());
                }
            }

            return new SendMessage(chatId, st.toString());
        } else {
            return new SendMessage(chatId, NOTHING_TO_TRACK);
        }

    }

    @Override
    public boolean supports(Update update) {
        return super.supports(update);
    }

    @Override
    public BotCommand toApiCommand() {
        return super.toApiCommand();
    }
}
