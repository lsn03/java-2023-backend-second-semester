package edu.java.configuration;

import edu.java.service.GitHubService;
import edu.java.service.StackOverFlowService;
import edu.java.service.client.GitHubClient;
import edu.java.service.client.StackOverFlowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubService();
    }

    @Bean
    public StackOverFlowClient stackOverFlowClient() {
        return new StackOverFlowService();
    }
}
