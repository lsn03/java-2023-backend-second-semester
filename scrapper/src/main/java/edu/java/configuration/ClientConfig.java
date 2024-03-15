package edu.java.configuration;

import edu.java.service.BotHttpClient;
import edu.java.service.client.GitHubHttpClientService;
import edu.java.service.client.StackOverFlowHttpClientServiceService;
import edu.java.service.client.GitHubClient;
import edu.java.service.client.StackOverFlowClient;
import edu.java.util.Utils;
import lombok.AllArgsConstructor;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ClientConfig {
    private final ClientProperties properties;

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubHttpClientService(properties.getValue(Utils.GITHUB_BASE_URL));
    }

    @Bean
    public StackOverFlowClient stackOverFlowClient() {
        return new StackOverFlowHttpClientServiceService(properties.getValue(Utils.SOF_BASE_URL));
    }

    @Bean
    public BotHttpClient botHttpClient() {
        return new BotHttpClient(properties.getValue(Utils.BOT_BASE_URL));
    }

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }
}
