package edu.java.configuration;

import edu.java.service.GitHubService;
import edu.java.service.StackOverFlowService;
import edu.java.service.client.GitHubClient;
import edu.java.service.client.StackOverFlowClient;
import edu.java.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ClientConfig {
    private final ClientProperties properties;

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubService(properties.getValue(Utils.GITHUB_BASE_URL));
    }

    @Bean
    public StackOverFlowClient stackOverFlowClient() {
        return new StackOverFlowService(properties.getValue(Utils.SOF_BASE_URL));
    }
}
