package edu.java.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.BotService;
import edu.java.bot.configuration.kafka.KafkaProperties;
import edu.java.bot.model.dto.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "app.kafka", name = "using-queue", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class UpdateListener {
    private final KafkaProperties properties;
    private final BotService botService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final StringBuilder stringBuilder = new StringBuilder();

    @KafkaListener(
        topics = "#{kafkaProperties.topic}",
        groupId = "#{kafkaProperties.groupId}")
    @SneakyThrows
    public void listenUsualQueue(String message) {
        try {
            processMessage(properties.topic(), message);

        } catch (Exception e) {
            log.error("Send to DLQ message={}, exception: {}", message, e);
            kafkaTemplate.send(properties.topicDlq(), message);
        }

    }

    @KafkaListener(topics = "#{kafkaProperties.topicDlq}", groupId = "#{kafkaProperties.groupId}")
    public void listenDeadLetterQueue(String message) {
        try {

            processMessage(properties.topicDlq(), message);
        } catch (Exception e) {

            log.error("Error while handling message from DLQ, message={}, exception: {}", message, e);
        }
    }

    private void processMessage(String topic, String message) throws Exception {
        LinkUpdateRequest linkUpdateRequest = objectMapper.readValue(message, LinkUpdateRequest.class);
        log.info("[BOT] Received from topic={}, message={}, data={}", topic, message, linkUpdateRequest);
        stringBuilder.setLength(0);
        stringBuilder.append("New changes at link ").append(linkUpdateRequest.getUrl())
            .append(System.lineSeparator()).append(linkUpdateRequest.getDescription());

        for (var chatId : linkUpdateRequest.getTgChatIds()) {
            botService.myExecute(new SendMessage(chatId, stringBuilder.toString()));
        }
    }
}
