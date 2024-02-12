package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UnTrackCommand extends AbstractCommand{
    public static final String INPUT_URL_FOR_UN_TRACK = "Пожалуйста, отправьте URL для прекращения отслеживания.";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UnTrackCommand() {
        super("/untrack");
    }

    @Override
    public String command() {
        return super.command;
    }

    @Override
    public String description() {
        return "Прекратить отслеживание ссылки";
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

        return new SendMessage(chatId, INPUT_URL_FOR_UN_TRACK);
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
