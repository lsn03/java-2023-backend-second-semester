package edu.java.bot.configuration;

import edu.java.bot.service.client.ScrapperHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    private final ScrapperProperties scrapperProperties;

    public ClientConfiguration(@Autowired ScrapperProperties scrapperProperties) {
        this.scrapperProperties = scrapperProperties;
    }

    @Bean
    public ScrapperHttpClient scrapperHttpClient() {
        return new ScrapperHttpClient(scrapperProperties.getBaseUrl());
    }
}
