package edu.java.configuration;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(Scheduler scheduler) {
    public record Scheduler(boolean enable, Duration interval, Duration forceCheckDelay) {
    }
}

