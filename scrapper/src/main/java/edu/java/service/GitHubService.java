package edu.java.service;

import edu.java.model.github.PullRequestModelResponse;
import edu.java.model.github.dto.IssueCommentDtoResponse;
import edu.java.model.github.dto.PullCommentDtoResponse;
import edu.java.model.github.dto.PullCommitDtoResponse;
import edu.java.model.github.dto.PullReviewDtoResponse;
import edu.java.service.client.GitHubClient;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

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
    public PullRequestModelResponse fetchPullRequest(String owner, String repo, int pullNumber) {
        List<IssueCommentDtoResponse> issueComments = getIssueComments(owner, repo, pullNumber);
        List<PullReviewDtoResponse> pullReviews = getPullReviews(owner, repo, pullNumber);
        List<PullCommentDtoResponse> pullComments = getPullComments(owner, repo, pullNumber);
        List<PullCommitDtoResponse> pullCommits = getAllCommitsInPullRequest(owner, repo, pullNumber);

        PullRequestModelResponse model = new PullRequestModelResponse();
        model.setIssueCommentDTOS(issueComments);
        model.setPullReviewDTOS(pullReviews);
        model.setPullCommentDTOS(pullComments);
        model.setPullCommitDTOS(pullCommits);
        return model;

    }

    @Override
    public List<IssueCommentDtoResponse> getIssueComments(String owner, String repo, int pullNumber) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}/issues/{pullNumber}/comments", owner, repo, pullNumber)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<IssueCommentDtoResponse>>() {
            }).block();

    }

    @Override
    public List<PullReviewDtoResponse> getPullReviews(String owner, String repo, int pullNumber) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}/pulls/{pullNumber}/reviews", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<List<PullReviewDtoResponse>>() {
            }).block();
    }

    @Override
    public List<PullCommentDtoResponse> getPullComments(String owner, String repo, int pullNumber) {
        return webClient.get()
            .uri("repos/{owner}/{repo}/pulls/{pullNumber}/comments", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<List<PullCommentDtoResponse>>() {
            }).block();

    }

    @Override
    public List<PullCommitDtoResponse> getAllCommitsInPullRequest(String owner, String repo, int pullNumber) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}/pulls/{pullNumber}/commits", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<List<PullCommitDtoResponse>>() {
            }).block();
    }
}
