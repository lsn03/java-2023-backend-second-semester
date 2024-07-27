package edu.java.bot.hw8;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.BotService;
import edu.java.bot.configuration.kafka.KafkaProperties;
import edu.java.bot.controller.UpdateListener;
import edu.java.bot.model.dto.request.LinkUpdateRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateListenerMockTest {
    UpdateListener updateListener;
    KafkaProperties properties = new KafkaProperties(null, null, null, "topic", "topic_dlq", "group", "servers");

    @Mock
    BotService botMock;
    @Mock
    ObjectMapper mapperMock;
    @Mock
    KafkaTemplate<String, String> kafkaTemplateMock;

    @SneakyThrows
    @Test
    public void testUsualQueue() {
        updateListener = new UpdateListener(properties, botMock, mapperMock, kafkaTemplateMock);
        var e = new Exception();
        when(mapperMock.readValue(anyString(), (JavaType) any(Object.class))).thenReturn(e);


        Assertions.assertDoesNotThrow(() -> updateListener.listenUsualQueue("message"));

    }

    @SneakyThrows
    @Test
    public void testDlq() {
        updateListener = new UpdateListener(properties, botMock, mapperMock, kafkaTemplateMock);
        when(mapperMock.readValue(anyString(), (JavaType) any(Object.class))).thenReturn(new LinkUpdateRequest());

        Assertions.assertDoesNotThrow(() -> updateListener.listenDeadLetterQueue("message"));

    }
}
