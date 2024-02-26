package edu.java.service;

import edu.java.model.github.PullRequestModelResponse;
import edu.java.model.github.dto.IssueCommentDTOResponse;
import edu.java.model.github.dto.PullCommentDTOResponse;
import edu.java.model.github.dto.PullCommitDTOResponse;
import edu.java.model.github.dto.PullReviewDTOResponse;
import edu.java.service.client.GitHubClient;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GitHubService implements GitHubClient {
    private static final String GITHUB_TOKEN = System.getenv().get("APP_GITHUB_TOKEN");
    private final WebClient webClient;

    public GitHubService(String url) {

        webClient = WebClient.builder()
            .baseUrl(url)
            .defaultHeader("User-Agent", "lsn03")
            .defaultHeader("Accept", "application/vnd.github+json")
            .defaultHeader("Authorization", "token " + GITHUB_TOKEN)
            .build();
    }

    @Override
    public Mono<PullRequestModelResponse> fetchPullRequest(String owner, String repo, int pullNumber) {
        Mono<List<IssueCommentDTOResponse>> issueCommentsMono = getIssueComments(owner, repo, pullNumber);
        Mono<List<PullReviewDTOResponse>> pullReviewsMono = getPullReviews(owner, repo, pullNumber);
        Mono<List<PullCommentDTOResponse>> pullCommentsMono = getPullComments(owner, repo, pullNumber);
        Mono<List<PullCommitDTOResponse>> pullCommitsMono = getAllCommitsInPullRequest(owner, repo, pullNumber);

        return Mono.zip(issueCommentsMono, pullReviewsMono, pullCommentsMono, pullCommitsMono)
            .map(objects -> {
                PullRequestModelResponse model = new PullRequestModelResponse();
                model.setIssueCommentDTOS(objects.getT1());
                model.setPullReviewDTOS(objects.getT2());
                model.setPullCommentDTOS(objects.getT3());
                model.setPullCommitDTOS(objects.getT4());
                return model;
            });
    }

    @Override
    public Mono<List<IssueCommentDTOResponse>> getIssueComments(String owner, String repo, int pullNumber) {
        return webClient.get().uri("/repos/{owner}/{repo}/issues/{pullNumber}/comments", owner, repo, pullNumber)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<>() {
            });

    }

    @Override
    public Mono<List<PullReviewDTOResponse>> getPullReviews(String owner, String repo, int pullNumber) {
        return webClient.get().uri("/repos/{owner}/{repo}/pulls/{pullNumber}/reviews", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
            });
    }

    @Override
    public Mono<List<PullCommentDTOResponse>> getPullComments(String owner, String repo, int pullNumber) {
        return webClient.get().uri("repos/{owner}/{repo}/pulls/{pullNumber}/comments", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
            });

    }

    @Override
    public Mono<List<PullCommitDTOResponse>> getAllCommitsInPullRequest(String owner, String repo, int pullNumber) {
        return webClient.get().uri("/repos/{owner}/{repo}/pulls/{pullNumber}/commits", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
            });
    }
}
