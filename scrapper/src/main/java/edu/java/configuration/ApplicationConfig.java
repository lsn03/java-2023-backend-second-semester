package edu.java.configuration;

import edu.java.configuration.access.AccessType;
import edu.java.configuration.kafka.KafkaProperties;
import edu.java.configuration.rate.BucketProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(Scheduler scheduler,
                                BucketProperties rate,
                                KafkaProperties kafka,
                                AccessType databaseAccessType,
                                GitHubApiProperties gitHubApiProperties,
                                StackOverFlowApiProperties stackOverFlowApiProperties) {
    @Bean
    public long schedulerInterval() {
        return scheduler.interval.toMillis();
    }

    @Bean
    public KafkaProperties kafkaProperties() {
        return kafka;
    }

    @Bean
    public Bucket getBucket() {
        return Bucket.builder()
            .addLimit(Bandwidth.classic(
                rate.count(),
                Refill.intervally(rate.count(), Duration.ofSeconds(rate.seconds()))
            ))
            .build();
    }

    public record Scheduler(boolean enable, Duration interval, Duration forceCheckDelay) {
    }

    public record GitHubApiProperties(String token) {
    }

    public record StackOverFlowApiProperties(String token, String key) {
    }
}

