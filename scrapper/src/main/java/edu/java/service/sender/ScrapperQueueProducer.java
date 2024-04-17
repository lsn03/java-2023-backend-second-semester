package edu.java.service.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.configuration.ApplicationConfig;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@Slf4j
public class ScrapperQueueProducer implements Sender {
    private final ApplicationConfig configuration;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void send(LinkUpdateRequest update) {
        String message = objectMapper.writeValueAsString(update);
        log.info("Send to topic={}, message={}", configuration.kafka().topic(), message);
        kafkaTemplate.send(configuration.kafka().topic(), message);
    }
}
