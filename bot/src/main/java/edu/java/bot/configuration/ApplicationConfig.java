package edu.java.bot.configuration;

import edu.java.bot.configuration.kafka.KafkaProperties;
import edu.java.bot.configuration.rate.BucketProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.validation.constraints.NotEmpty;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    BucketProperties rate,
    KafkaProperties kafka,
    RetryConfig retry,
    String scrapperBaseUrl
) {
    @Bean
    public Bucket getBucket() {
        return Bucket.builder()
            .addLimit(Bandwidth.classic(
                rate.count(),
                Refill.intervally(rate.count(), Duration.ofSeconds(rate.seconds()))
            ))
            .build();
    }

    @Bean
    public ScrapperProperties scrapperProperties() {
        return new ScrapperProperties(scrapperBaseUrl, retry);
    }

    public record ScrapperProperties(String scrapperBaseUrl, RetryConfig retryConfig) {

    }
}
