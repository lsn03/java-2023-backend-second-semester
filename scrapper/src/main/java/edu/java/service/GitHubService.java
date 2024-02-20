package edu.java.service;

import edu.java.model.github.PullRequestModel;
import edu.java.model.github.dto.IssueCommentDTO;
import edu.java.model.github.dto.PullCommentDTO;
import edu.java.model.github.dto.PullCommitDTO;
import edu.java.model.github.dto.PullReviewDTO;
import edu.java.service.client.BaseUrl;
import edu.java.service.client.GitHubClient;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GitHubService implements GitHubClient {
    private static final String GITHUB_TOKEN = System.getenv().get("APP_GITHUB_TOKEN");
    private final WebClient webClient;

    public GitHubService() {
        this(BaseUrl.GITHUB_BASE_URL.getUrl());
    }

    public GitHubService(String url) {
        String githubApiBaseUrl = BaseUrl.GITHUB_BASE_URL.getUrl();

        if (!url.isEmpty() && !url.isBlank()) {
            githubApiBaseUrl = url;
        }
        webClient = WebClient.builder()
            .baseUrl(githubApiBaseUrl)
            .defaultHeader("User-Agent", "lsn03")
            .defaultHeader("Accept", "application/vnd.github+json")
            .defaultHeader("Authorization", "token " + GITHUB_TOKEN)
            .build();
    }

    @Override
    public Mono<PullRequestModel> fetchPullRequest(String owner, String repo, int pullNumber) {
        Mono<List<IssueCommentDTO>> issueCommentsMono = getIssueComments(owner, repo, pullNumber);
        Mono<List<PullReviewDTO>> pullReviewsMono = getPullReviews(owner, repo, pullNumber);
        Mono<List<PullCommentDTO>> pullCommentsMono = getPullComments(owner, repo, pullNumber);
        Mono<List<PullCommitDTO>> pullCommitsMono = getAllCommitsInPullRequest(owner, repo, pullNumber);

        return Mono.zip(issueCommentsMono, pullReviewsMono, pullCommentsMono, pullCommitsMono)
            .map(objects -> {
                PullRequestModel model = new PullRequestModel();
                model.setIssueCommentDTOS(objects.getT1());
                model.setPullReviewDTOS(objects.getT2());
                model.setPullCommentDTOS(objects.getT3());
                model.setPullCommitDTOS(objects.getT4());
                return model;
            });
    }

    @Override
    public Mono<List<IssueCommentDTO>> getIssueComments(String owner, String repo, int pullNumber) {
        return webClient.get().uri("/repos/{owner}/{repo}/issues/{pullNumber}/comments", owner, repo, pullNumber)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<>() {
            });

    }

    @Override
    public Mono<List<PullReviewDTO>> getPullReviews(String owner, String repo, int pullNumber) {
        return webClient.get().uri("/repos/{owner}/{repo}/pulls/{pullNumber}/reviews", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
            });
    }

    @Override
    public Mono<List<PullCommentDTO>> getPullComments(String owner, String repo, int pullNumber) {
        return webClient.get().uri("repos/{owner}/{repo}/pulls/{pullNumber}/comments", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
            });

    }

    @Override
    public Mono<List<PullCommitDTO>> getAllCommitsInPullRequest(String owner, String repo, int pullNumber) {
        return webClient.get().uri("/repos/{owner}/{repo}/pulls/{pullNumber}/commits", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
            });
    }
}
