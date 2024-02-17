package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated

@Setter
@Getter
@Configuration
@EnableScheduling
@ConfigurationProperties(prefix = "app")
public class ApplicationConfig {
    private Scheduler scheduler;

    public ApplicationConfig() {
        scheduler = new Scheduler();
    }

    @Setter
    @Getter
    public static class Scheduler {
        private boolean enable;
        private Duration interval;
        private Duration forceCheckDelay;
    }
}
