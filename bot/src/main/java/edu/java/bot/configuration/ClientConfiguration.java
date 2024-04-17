package edu.java.bot.configuration;

import edu.java.bot.service.client.ScrapperHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final ApplicationConfig applicationConfig;

    @Bean
    public ScrapperHttpClient scrapperHttpClient() {
        return new ScrapperHttpClient(applicationConfig.scrapperProperties());
    }
}
