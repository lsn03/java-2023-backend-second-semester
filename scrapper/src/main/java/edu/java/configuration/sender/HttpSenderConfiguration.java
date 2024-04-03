package edu.java.configuration.sender;

import edu.java.service.client.BotHttpClient;
import edu.java.service.sender.ScrapperHttpSender;
import edu.java.service.sender.Sender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "kafka.using-queue", havingValue = "false")
@RequiredArgsConstructor
@Slf4j
public class HttpSenderConfiguration {
    private final BotHttpClient client;
    @Bean
    public Sender httpSender(){
        log.info("creating ScrapperHttpSender");
        return new ScrapperHttpSender(client);
    }
}
