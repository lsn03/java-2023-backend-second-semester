package edu.java.bot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.BotService;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.model.dto.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.kafka.using-queue", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class UpdateListener {
    private final ApplicationConfig config;
    private final BotService botService;
    private final ObjectMapper objectMapper;
    private final StringBuilder stringBuilder = new StringBuilder();
    @KafkaListener(topics = "topic1",
                   groupId = "my-group-id")
    @SneakyThrows
    public void receive(String message) {
        LinkUpdateRequest linkUpdateRequest = objectMapper.readValue(message, LinkUpdateRequest.class);
        log.info("[BOT] Received from topic={}, message={}, data={}",config.kafka().topic(), message,linkUpdateRequest);
        stringBuilder.setLength(0);
        stringBuilder.append("New changes at link ").append(linkUpdateRequest.getUrl())
            .append(System.lineSeparator()).append(linkUpdateRequest.getDescription());

        for (var chatId : linkUpdateRequest.getTgChatIds()) {
            botService.myExecute(new SendMessage(chatId, stringBuilder.toString()));
        }

    }
}
