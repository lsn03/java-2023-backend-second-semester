package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.storage.Storage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HelpCommand implements Command {
    public static final String USER_NOT_REGISTERED =
        "Вы не зарегистрированы. Функционал бота не доступен. Введите /start для регистрации.";

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private final StringBuilder stringBuilder;
    private final List<Command> commandList;
    private final Storage storage;


    public HelpCommand(List<Command> commandList, Storage storage) {

        this.storage = storage;
        this.stringBuilder = new StringBuilder();
        this.commandList = commandList;
        this.commandList.add(this);
        initOutputMessage();
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Показывает меню помощи";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        log.info(
            "User @{} entered \"{}\" user_id={}",
            update.message().chat().username(),
            update.message().text(),
            chatId
        );
        if (!storage.isUserAuth(chatId)) {
            return new SendMessage(chatId, USER_NOT_REGISTERED);
        }
        return new SendMessage(chatId, stringBuilder.toString());

    }

    @Override
    public boolean supports(Update update) {
        return Command.super.supports(update);
    }

    @Override
    public BotCommand toApiCommand() {
        return Command.super.toApiCommand();
    }


    private void initOutputMessage() {
        stringBuilder.append("Бот позволяет отслеживать обновления сайтов.").append(LINE_SEPARATOR);
        stringBuilder.append("Поддерживаются сайты: StackOverFlow, Github.").append(LINE_SEPARATOR);
        stringBuilder.append("Примеры корректных ссылок:").append(LINE_SEPARATOR);
        stringBuilder.append("1. https://github.com/lsn03").append(LINE_SEPARATOR);
        stringBuilder.append("2. https://stackoverflow.com/questions/6402162/how-to-enable-intellij-hot-code-swap")
            .append(LINE_SEPARATOR);
        stringBuilder.append("Примеры некорректных ссылок:").append(LINE_SEPARATOR);
        stringBuilder.append("1. github.com/lsn03").append(LINE_SEPARATOR);
        stringBuilder.append("2. stackoverflow.com/questions/6402162/how-to-enable-intellij-hot-code-swap")
            .append(LINE_SEPARATOR);
        stringBuilder.append("3. vk.com").append(LINE_SEPARATOR);
        stringBuilder.append("Поддерживаемые команды:").append(LINE_SEPARATOR);

        for (int i = 0; i < commandList.size(); i++) {
            var elem = commandList.get(i);
            stringBuilder.append(i + 1).append(". ").append(elem.command()).append(" ").append(elem.description())
                .append(LINE_SEPARATOR);
        }
    }
}
