package edu.java.bot.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MicrometerConfig {
    private final MeterRegistry meterRegistry;

    @Bean
    public Counter counter() {
        return Counter.builder("processed_messages").description("Count of sent messages to Telegram")
            .register(meterRegistry);
    }
}
