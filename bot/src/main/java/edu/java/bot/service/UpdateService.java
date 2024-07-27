package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.BotService;
import edu.java.bot.model.dto.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateService {
    private final BotService botService;
    private final StringBuilder stringBuilder = new StringBuilder();

    public void sendUpdate(LinkUpdateRequest request) {
        stringBuilder.setLength(0);
        stringBuilder.append("New changes at link ").append(request.getUrl())
            .append(System.lineSeparator()).append(request.getDescription());

        for (var chatId : request.getTgChatIds()) {
            botService.myExecute(new SendMessage(chatId, stringBuilder.toString()));
        }
    }
}
