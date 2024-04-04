package edu.java.bot.configuration.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Configuration
@Component
@ConditionalOnProperty(prefix = "app.kafka", name = "using-queue", havingValue = "true")
@Slf4j
@RequiredArgsConstructor
public class KafkaConfiguration {
    private final ApplicationConfig config;

    @Bean
    public KafkaProperties kafkaProperties(){
        return config.kafka();
    }
    @Bean
    public NewTopic dlqTopic() {
        return new NewTopic(
            config.kafka().topicDlq(),
            config.kafka().partitions(),
            config.kafka().replicas().shortValue()
        );
    }
    @Bean
    public NewTopic topic() {
        return new NewTopic(
            config.kafka().topic(),
            config.kafka().partitions(),
            config.kafka().replicas().shortValue()
        );
    }


    @Bean
    public KafkaTemplate<String, String> stringMessageKafkaTemplate() {
        log.info("create stringMessageKafkaTemplate");
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(
            Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                config.kafka().servers(),
//                "localhost:29091,localhost:29092,localhost:29093",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
//                ProducerConfig.ACKS_CONFIG, "all",
//                ProducerConfig.RETRIES_CONFIG, 3
            )));
    }

}
