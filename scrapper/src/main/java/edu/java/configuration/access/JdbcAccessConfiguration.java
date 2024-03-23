package edu.java.configuration.access;

import edu.java.domain.repository.ChatRepository;
import edu.java.domain.repository.GitHubRepository;
import edu.java.domain.repository.LinkChatRepository;
import edu.java.domain.repository.LinkRepository;
import edu.java.domain.repository.StackOverFlowRepository;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.domain.repository.jdbc.JdbcGitHubRepository;
import edu.java.domain.repository.jdbc.JdbcLinkChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.domain.repository.jdbc.JdbcStackOverFlowRepository;
import edu.java.service.database.GitHubService;
import edu.java.service.database.LinkService;
import edu.java.service.database.StackOverFlowService;
import edu.java.service.database.TgChatService;
import edu.java.service.database.jdbc.JdbcGitHubService;
import edu.java.service.database.jdbc.JdbcLinkService;
import edu.java.service.database.jdbc.JdbcStackOverFlowService;
import edu.java.service.database.jdbc.JdbcTgChatService;
import edu.java.service.handler.Handler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
@RequiredArgsConstructor
public class JdbcAccessConfiguration {
    private final JdbcTemplate jdbcTemplate;
    private final List<Handler> handlers;

    @Bean
    public LinkChatRepository jdbcLinkChatRepository() {
        return new JdbcLinkChatRepository(jdbcTemplate);
    }

    @Bean
    public LinkRepository jdbcLinkRepository() {
        return new JdbcLinkRepository(jdbcTemplate, (JdbcLinkChatRepository) jdbcLinkChatRepository());
    }

    @Bean
    public ChatRepository jdbcChatRepository() {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    public LinkService jdbcLinkService() {
        return new JdbcLinkService(
            (JdbcLinkRepository) jdbcLinkRepository(),
            (JdbcLinkChatRepository) jdbcLinkChatRepository(),
            handlers
        );
    }

    @Bean
    public TgChatService jdbcTgChatService() {
        return new JdbcTgChatService((JdbcChatRepository) jdbcChatRepository());
    }

    @Bean
    public GitHubRepository jdbcGitHubRepository() {
        return new JdbcGitHubRepository(jdbcTemplate);
    }

    @Bean
    public StackOverFlowRepository jdbcStackOverFlowRepository() {
        return new JdbcStackOverFlowRepository(jdbcTemplate);
    }

    @Bean
    public GitHubService jdbcGitHubService() {
        return new JdbcGitHubService((JdbcGitHubRepository) jdbcGitHubRepository());
    }

    @Bean
    public StackOverFlowService jdbcStackOverFlowService() {
        return new JdbcStackOverFlowService((JdbcStackOverFlowRepository) jdbcStackOverFlowRepository());
    }
}
