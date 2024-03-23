package edu.java.configuration.access;

import edu.java.domain.repository.ChatRepository;
import edu.java.domain.repository.GitHubRepository;
import edu.java.domain.repository.LinkChatRepository;
import edu.java.domain.repository.LinkRepository;
import edu.java.domain.repository.StackOverFlowRepository;
import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.domain.repository.jpa.JpaChatRepositoryInterface;
import edu.java.domain.repository.jpa.JpaGitHubRepository;
import edu.java.domain.repository.jpa.JpaGitHubRepositoryInterface;
import edu.java.domain.repository.jpa.JpaLinkChatRepository;
import edu.java.domain.repository.jpa.JpaLinkChatRepositoryInterface;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.domain.repository.jpa.JpaLinkRepositoryInterface;
import edu.java.domain.repository.jpa.JpaStackOverFlowRepository;
import edu.java.domain.repository.jpa.JpaStackOverFlowRepositoryInterface;
import edu.java.service.database.GitHubService;
import edu.java.service.database.LinkService;
import edu.java.service.database.StackOverFlowService;
import edu.java.service.database.TgChatService;
import edu.java.service.database.jpa.JpaGitHubService;
import edu.java.service.database.jpa.JpaLinkService;
import edu.java.service.database.jpa.JpaStackOverFlowService;
import edu.java.service.database.jpa.JpaTgChatService;
import edu.java.service.handler.Handler;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
@RequiredArgsConstructor
public class JpaAccessConfiguration {
    private final List<Handler> handlers;
    private final EntityManager entityManager;
    private final JpaChatRepositoryInterface jpaChatRepositoryInterface;
    private final JpaLinkRepositoryInterface jpaLinkRepositoryInterface;
    private final JpaLinkChatRepositoryInterface jpaLinkChatRepositoryInterface;

    private final JpaGitHubRepositoryInterface jpaGitHubRepositoryInterface;
    private final JpaStackOverFlowRepositoryInterface jpaStackOverFlowRepositoryInterface;

    @Bean
    public LinkChatRepository jpaLinkChatRepository() {
        return new JpaLinkChatRepository(
            jpaLinkChatRepositoryInterface,
            entityManager,
            jpaLinkRepositoryInterface,
            jpaChatRepositoryInterface
        );
    }

    @Bean
    public LinkRepository jpaLinkRepository() {
        return new JpaLinkRepository(
            jpaLinkRepositoryInterface,
            entityManager,
            (JpaLinkChatRepository) jpaLinkChatRepository()
        );
    }

    @Bean
    public ChatRepository jpaChatRepository() {
        return new JpaChatRepository(jpaChatRepositoryInterface);
    }

    @Bean
    public LinkService jpaLinkService() {
        return new JpaLinkService(
            (JpaLinkRepository) jpaLinkRepository(),
            (JpaLinkChatRepository) jpaLinkChatRepository(),
            handlers
        );
    }

    @Bean
    public TgChatService jpaTgChatService() {
        return new JpaTgChatService((JpaChatRepository) jpaChatRepository());
    }

    @Bean
    public GitHubRepository jpaGitHubRepository() {
        return new JpaGitHubRepository(entityManager, jpaGitHubRepositoryInterface);
    }

    @Bean
    public StackOverFlowRepository jpaStackOverFlowRepository() {
        return new JpaStackOverFlowRepository(entityManager, jpaStackOverFlowRepositoryInterface);
    }

    @Bean
    public GitHubService jpaGitHubService() {
        return new JpaGitHubService((JpaGitHubRepository) jpaGitHubRepository());
    }

    @Bean
    public StackOverFlowService jpaStackOverFlowService() {
        return new JpaStackOverFlowService((JpaStackOverFlowRepository) jpaStackOverFlowRepository());
    }
}
