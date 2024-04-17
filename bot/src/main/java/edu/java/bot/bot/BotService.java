package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.command.Command;
import edu.java.bot.configuration.ApplicationConfig;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j

public class BotService implements Bot {
    private final TelegramBot telegramBot;
    private final List<Command> commandList;
    private final ApplicationConfig applicationConfig;
    private final BotFactory botFactory;

    public BotService(List<Command> commandList, ApplicationConfig applicationConfig, BotFactory botFactory) {
        this.commandList = commandList;
        this.applicationConfig = applicationConfig;
        this.botFactory = botFactory;

        telegramBot = this.botFactory.create(applicationConfig.telegramToken());
        log.info("Create BotService");
    }


    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void myExecute(BaseRequest<T, R> request) {

        log.info("myExecute {}", request.getParameters());
        telegramBot.execute(request);

    }

    @Override
    public int process(List<Update> list) {
        log.info("process {}", list);
        return 0;
    }

    @Override
    public void start() {
        log.info("start");
    }

    @Override
    public void close() throws Exception {
        log.info("close");
        telegramBot.shutdown();
    }

    public void setUpdatesListener(UpdatesListener listener) {
        telegramBot.setUpdatesListener(listener, new GetUpdates());
    }

    @EventListener(ApplicationReadyEvent.class)
    private void setUpCommands() {
        log.info("set up commands");
        BotCommand[] botCommands = commandList.stream().map(command -> new BotCommand(
            command.command(),
            command.description()
        )).toArray(BotCommand[]::new);

        telegramBot.execute(new SetMyCommands(botCommands));
    }
}
