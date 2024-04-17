package edu.java.configuration;

import edu.java.configuration.access.AccessType;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(Scheduler scheduler,
                                AccessType databaseAccessType,
                                GitHubApiProperties gitHubApiProperties,
                                StackOverFlowApiProperties stackOverFlowApiProperties) {
    @Bean
    public long schedulerInterval() {
        return scheduler.interval.toMillis();
    }

    public record Scheduler(boolean enable, Duration interval, Duration forceCheckDelay) {
    }

    public record GitHubApiProperties(String token) {
    }

    public record StackOverFlowApiProperties(String token, String key) {
    }
}

