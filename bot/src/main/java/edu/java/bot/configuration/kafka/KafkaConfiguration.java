package edu.java.bot.configuration.kafka;

import edu.java.bot.configuration.ApplicationConfig;
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

import java.util.Map;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "using-queue", havingValue = "true")
@Slf4j
@RequiredArgsConstructor
public class KafkaConfiguration {
    private final ApplicationConfig config;
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
            )));
    }

}
