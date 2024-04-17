package edu.java.configuration.access;

import edu.java.domain.repository.ChatRepository;
import edu.java.domain.repository.GitHubRepository;
import edu.java.domain.repository.LinkChatRepository;
import edu.java.domain.repository.LinkRepository;
import edu.java.domain.repository.StackOverFlowRepository;
import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.domain.repository.jooq.JooqGitHubRepository;
import edu.java.domain.repository.jooq.JooqLinkChatRepository;
import edu.java.domain.repository.jooq.JooqLinkRepository;
import edu.java.domain.repository.jooq.JooqStackOverFlowRepository;
import edu.java.service.database.GitHubService;
import edu.java.service.database.LinkService;
import edu.java.service.database.StackOverFlowService;
import edu.java.service.database.TgChatService;
import edu.java.service.database.jooq.JooqGitHubService;
import edu.java.service.database.jooq.JooqLinkService;
import edu.java.service.database.jooq.JooqStackOverFlowService;
import edu.java.service.database.jooq.JooqTgChatService;
import edu.java.service.handler.Handler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    private final DSLContext dslContext;
    private final List<Handler> handlers;

    @Bean
    public LinkChatRepository jooqLinkChatRepository() {
        return new JooqLinkChatRepository(dslContext);
    }

    @Bean
    public LinkRepository jooqLinkRepository() {
        return new JooqLinkRepository(dslContext, (JooqLinkChatRepository) jooqLinkChatRepository());
    }

    @Bean
    public ChatRepository jooqChatRepository() {
        return new JooqChatRepository(dslContext);
    }

    @Bean
    public LinkService jdbcLinkService() {
        return new JooqLinkService(
            (JooqLinkRepository) jooqLinkRepository(),
            (JooqLinkChatRepository) jooqLinkChatRepository(),
            handlers
        );
    }

    @Bean
    public TgChatService jooqTgChatService() {
        return new JooqTgChatService((JooqChatRepository) jooqChatRepository());
    }

    @Bean
    public GitHubRepository jooqGitHubRepository() {
        return new JooqGitHubRepository(dslContext, (JooqLinkRepository) jooqLinkRepository());
    }

    @Bean
    public StackOverFlowRepository jooqStackOverFlowRepository() {
        return new JooqStackOverFlowRepository(dslContext, (JooqLinkRepository) jooqLinkRepository());
    }

    @Bean
    public GitHubService jooqGitHubService() {
        return new JooqGitHubService((JooqGitHubRepository) jooqGitHubRepository());
    }

    @Bean
    public StackOverFlowService jooqStackOverFlowService() {
        return new JooqStackOverFlowService((JooqStackOverFlowRepository) jooqStackOverFlowRepository());
    }
}
