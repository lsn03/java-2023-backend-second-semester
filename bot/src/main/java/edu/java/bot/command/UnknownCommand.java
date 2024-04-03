package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnknownCommand implements Command {

    @Override
    public String command() {
        return "/";
    }

    @Override
    public String description() {
        return "Неизвестная комманда.";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        CommandUtils.extractMessageForLog(update, log);

        return new SendMessage(chatId, CommandUtils.UNKNOWN_COMMAND_HELP);
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
