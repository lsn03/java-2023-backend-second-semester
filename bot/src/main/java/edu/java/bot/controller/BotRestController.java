package edu.java.bot.controller;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.BotService;
import edu.java.bot.model.dto.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BotRestController {
    private final BotService botService;
    private StringBuilder stringBuilder = new StringBuilder();

    @PostMapping("/updates")
    public ResponseEntity<?> getUpdates(@RequestBody LinkUpdateRequest body) {
        stringBuilder.setLength(0);
        stringBuilder.append("New changes at link ").append(body.getUrl())
            .append(System.lineSeparator()).append(body.getDescription());

        for (var chatId : body.getTgChatIds()) {
            botService.myExecute(new SendMessage(chatId, stringBuilder.toString()));
        }

        return ResponseEntity.ok(body);
    }
}

