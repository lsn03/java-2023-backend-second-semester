package edu.java.configuration.kafka;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.configuration.ApplicationConfig;
import edu.java.service.sender.ScrapperQueueProducer;
import edu.java.service.sender.Sender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Configuration
@Component
@ConditionalOnProperty(prefix = "app.kafka", name = "using-queue", havingValue = "true")
@Slf4j
@RequiredArgsConstructor
public class KafkaConfiguration {
    private final ApplicationConfig config;
    private final ObjectMapper mapper;

    @Bean
    public Sender scrapperKafkaProducer(){
        log.info("creating ScrapperQueueProducer");
        return new ScrapperQueueProducer(config,stringMessageKafkaTemplate(),mapper);
    }
    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("topic1")
            .partitions(10)
            .replicas(1)
            .build();
    }



    @Bean
    public KafkaTemplate<String, String> stringMessageKafkaTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(
            Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:29091,localhost:29092,localhost:29093",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
//                ProducerConfig.ACKS_CONFIG, "all",
//                ProducerConfig.RETRIES_CONFIG, 3
//                ProducerConfig.PARTITIONER_CLASS_CONFIG, NamePartitioner.class
            )));
    }

//    @KafkaListener(topics = "topic1",
//                   groupId = "messages-group",
//                   containerFactory = "stringMessageKafkaListenerContainerFactory",
//                   concurrency = "1")
//    public void listenStringMessages(
//        @Payload String message,
//        @Header(KafkaHeaders.RECEIVED_KEY) String key,
//        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition
//    ) {
//        log.info("[SCRAPPER] Received Message from partition {} with key {}: {}", partition, key, message);
//    }
}
