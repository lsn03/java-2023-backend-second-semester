package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.CommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CommandService commandService;

    @Autowired
    public ListCommand(CommandService commandService) {
        this.commandService = commandService;
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
        logger.info(
            "User @{} entered \"{}\" user_id={}",
            update.message().chat().username(),
            update.message().text(),
            chatId
        );
        var list = commandService.getUserTracks(chatId);
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
            return new SendMessage(chatId, "Нет ссылок для отслеживания. Введите /track.");
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
}
