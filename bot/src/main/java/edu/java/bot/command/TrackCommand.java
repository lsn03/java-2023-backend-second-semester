package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exception.RepeatTrackException;
import edu.java.bot.exception.UnsupportedSiteException;
import edu.java.bot.processor.UserState;
import edu.java.bot.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrackCommand implements Command {

    private final Storage storage;

    @Autowired
    public TrackCommand(Storage storage) {

        this.storage = storage;

    }

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Начать отслеживание ссылки";
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
        if (!storage.isUserAuth(chatId)) {
            return new SendMessage(chatId, CommandUtils.USER_NOT_REGISTERED);
        }
        UserState state = storage.getUserState(chatId);
        return switch (state) {
            case AWAITING_URL_FOR_TRACK -> processAwaitingUrlState(chatId, text);
            case UNAUTHORIZED -> new SendMessage(chatId, CommandUtils.USER_NOT_REGISTERED);
            case DEFAULT -> processDefaultState(chatId, text);
            default -> new SendMessage(chatId, CommandUtils.EXCEPTION_MESSAGE);
        };

    }

    @Override
    public boolean supports(Update update) {
        return Command.super.supports(update);
    }

    @Override
    public BotCommand toApiCommand() {
        return Command.super.toApiCommand();
    }

    private SendMessage processDefaultState(Long chatId, String text) {

        if (text.equals(command())) {
            storage.setUserState(chatId, UserState.AWAITING_URL_FOR_TRACK);
            return new SendMessage(chatId, CommandUtils.INPUT_URL_FOR_TRACK);
        } else {
            throw new IllegalArgumentException();
        }

    }

    private SendMessage processAwaitingUrlState(Long chatId, String text) {
        if (!text.startsWith("/")) {
            SendMessage message = null;
            try {
                var result = storage.addUrl(chatId, text);
                if (result.isEmpty()) {
                    storage.setUserState(chatId, UserState.DEFAULT);
                    message = new SendMessage(chatId, CommandUtils.URL_SUCCESSFULLY_ADDED);
                } else {
                    if (result.get().getExceptionName().equals(RepeatTrackException.class.getSimpleName())) {
                        message = new SendMessage(chatId, CommandUtils.URL_ALREADY_EXIST);
                    }
                }
                return message;
            } catch (UnsupportedSiteException e) {
                return new SendMessage(chatId, e.getMessage() + CommandUtils.EXCEPTION_MESSAGE);
            }

        } else {
            return new SendMessage(chatId, CommandUtils.AWAITING_URL);
        }

    }
}
