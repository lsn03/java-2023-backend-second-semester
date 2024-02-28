package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processor.UserState;
import edu.java.bot.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CancelCommand implements Command {

    private final Storage storage;

    @Override
    public String command() {
        return "/cancel";
    }

    @Override
    public String description() {
        return "Отменяет ввод URL в командах /track и /untrack";
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
            return new SendMessage(chatId, CommandUtils.USER_NOT_REGISTERED);
        } else {
            UserState state = storage.getUserState(chatId);
            return switch (state) {
                case AWAITING_URL_FOR_UN_TRACK, AWAITING_URL_FOR_TRACK -> processCancel(chatId);
                default -> new SendMessage(chatId, CommandUtils.NOTHING_TO_CANCEL);
            };
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

    private SendMessage processCancel(Long chatId) {
        storage.setUserState(chatId, UserState.DEFAULT);
        return new SendMessage(chatId, CommandUtils.CANCEL_INPUT);
    }
}
