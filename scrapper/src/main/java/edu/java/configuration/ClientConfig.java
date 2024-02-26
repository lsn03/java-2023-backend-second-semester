package edu.java.configuration;

import edu.java.service.GitHubService;
import edu.java.service.StackOverFlowService;
import edu.java.service.client.GitHubClient;
import edu.java.service.client.StackOverFlowClient;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ClientConfig {
    private final ClientProperties properties;

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubService(properties.getGithubBaseUrl());
    }

    @Bean
    public StackOverFlowClient stackOverFlowClient() {
        return new StackOverFlowService(properties.getSofBaseUrl());
    }
}
