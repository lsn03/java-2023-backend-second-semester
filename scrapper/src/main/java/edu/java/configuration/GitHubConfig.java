package edu.java.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GitHubConfig {
    private static final String GITHUB_API_BASE_URL = "https://api.github.com";

    private static final String GITHUB_TOKEN = System.getenv().get("APP_GITHUB_TOKEN");

    @Bean
    public WebClient gitHubClient() {
        return WebClient.builder()
            .baseUrl(GITHUB_API_BASE_URL)
            .defaultHeader("User-Agent", "Awesome-Octocat-App")
            .defaultHeader("Accept", "application/vnd.github+json")
            .defaultHeader("Authorization", "token " + GITHUB_TOKEN)
            .build();

    }
}
