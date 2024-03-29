package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartCommand implements Command {



    private final Storage storage;

    @Autowired
    public StartCommand(Storage storage) {

        this.storage = storage;
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Регистрация в боте";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String username = update.message().chat().username();
        String text = update.message().text();
        log.info(
            "User @{} entered \"{}\" user_id={}",
            username,
            text,
            chatId
        );
        if (storage.isUserAuth(chatId)) {
            log.info("User @{} id={} already sign in", username, chatId);
            return new SendMessage(chatId, CommandUtils.ALREADY_AUTH);
        } else {
            try {
                storage.authUser(chatId);
                log.info("User @{} id={} was successfully registered", username, chatId);
                return new SendMessage(chatId, CommandUtils.USER_REGISTERED_SUCCESS);
            } catch (Exception e) {
                log.error(e.getMessage());
                return new SendMessage(chatId,"Произошла внутренняя ошибка сервиса, попробуйте позже");
            }

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
