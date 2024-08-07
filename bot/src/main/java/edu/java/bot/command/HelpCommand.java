package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.storage.Storage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HelpCommand implements Command {

    private static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String CORRECT_LINK_EXAMPLE = "Примеры корректной ссылки:";

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
        CommandUtils.extractMessageForLog(update, log);
        if (!storage.isUserAuth(chatId)) {
            return new SendMessage(chatId, CommandUtils.USER_NOT_REGISTERED);
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
        stringBuilder.append("Github: отслеживание новых коммитов в Pull Request").append(LINE_SEPARATOR);
        stringBuilder.append("Формат: https://github.com/{owner}/{repo}/pull/{pull_number}").append(LINE_SEPARATOR);
        stringBuilder.append(CORRECT_LINK_EXAMPLE).append(LINE_SEPARATOR);
        stringBuilder.append("1. https://github.com/lsn03/java-2023-backend-second-semester/pull/1")
            .append(LINE_SEPARATOR);
        stringBuilder.append("StackOverFlow: отслеживание новых ответов в вопросе").append(LINE_SEPARATOR);
        stringBuilder.append("Формат: https://stackoverflow.com/questions/{question_id}/*").append(LINE_SEPARATOR);
        stringBuilder.append(CORRECT_LINK_EXAMPLE).append(LINE_SEPARATOR);
        stringBuilder.append("1. https://stackoverflow.com/questions/6402162/how-to-enable-intellij-hot-code-swap")
            .append(LINE_SEPARATOR);

        stringBuilder.append("Примеры некорректных ссылок:").append(LINE_SEPARATOR);
        stringBuilder.append("1. github.com/lsn03").append(LINE_SEPARATOR);
        stringBuilder.append("2. stackoverflow.com/questions/6402162/how-to-enable-intellij-hot-code-swap")
            .append(LINE_SEPARATOR);
        stringBuilder.append("3. https://github.com/lsn03").append(LINE_SEPARATOR);
        stringBuilder.append("4. vk.com").append(LINE_SEPARATOR);
        stringBuilder.append("Поддерживаемые команды:").append(LINE_SEPARATOR);

        for (int i = 0; i < commandList.size(); i++) {
            var elem = commandList.get(i);
            stringBuilder.append(i + 1).append(". ").append(elem.command()).append(" ").append(elem.description())
                .append(LINE_SEPARATOR);
        }
    }
}
