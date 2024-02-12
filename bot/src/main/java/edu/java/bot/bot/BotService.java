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
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotService implements Bot {
    private final TelegramBot telegramBot;
    private final List<Command> commandList;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public BotService(List<Command> commandList) {
        this.commandList = commandList;
        telegramBot = new TelegramBot(System.getenv().get("APP_TELEGRAM_TOKEN"));
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void myExecute(BaseRequest<T, R> request) {

        logger.info("myExecute {}", request.getParameters());
        telegramBot.execute(request);

    }

    @Override
    public int process(List<Update> list) {
        logger.info("process {}", list);
        return 0;
    }

    @PostConstruct
    public void start() {

        BotCommand[] botCommands = commandList.stream().map(command -> new BotCommand(
            command.command(),
            command.description()
        )).toArray(BotCommand[]::new);

        telegramBot.execute(new SetMyCommands(botCommands));

    }

    @Override
    public void close() throws Exception {
        logger.info("close");
        telegramBot.shutdown();
    }

    public void setUpdatesListener(UpdatesListener listener) {
        telegramBot.setUpdatesListener(listener, new GetUpdates());
    }
}