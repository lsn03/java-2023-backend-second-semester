package edu.java.service.client;

import edu.java.model.github.PullRequestModelResponse;
import edu.java.model.github.dto.IssueCommentDTOResponse;
import edu.java.model.github.dto.PullCommentDTOResponse;
import edu.java.model.github.dto.PullCommitDTOResponse;
import edu.java.model.github.dto.PullReviewDTOResponse;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubHttpClientService implements GitHubClient {
    private static final String GITHUB_TOKEN = System.getenv().get("APP_GITHUB_TOKEN");
    private final WebClient webClient;

    public GitHubHttpClientService(String url) {

        webClient = WebClient.builder()
            .baseUrl(url)
            .defaultHeader("User-Agent", "lsn03")
            .defaultHeader("Accept", "application/vnd.github+json")
            .defaultHeader("Authorization", "token " + GITHUB_TOKEN)
            .build();
    }

    @Override
    public PullRequestModelResponse fetchPullRequest(String owner, String repo, int pullNumber) {
        List<IssueCommentDTOResponse> issueComments = getIssueComments(owner, repo, pullNumber);
        List<PullReviewDTOResponse> pullReviews = getPullReviews(owner, repo, pullNumber);
        List<PullCommentDTOResponse> pullComments = getPullComments(owner, repo, pullNumber);
        List<PullCommitDTOResponse> pullCommits = getAllCommitsInPullRequest(owner, repo, pullNumber);

        PullRequestModelResponse model = new PullRequestModelResponse();
        model.setIssueCommentDTOS(issueComments);
        model.setPullReviewDTOS(pullReviews);
        model.setPullCommentDTOS(pullComments);
        model.setPullCommitDTOS(pullCommits);
        return model;

    }

    @Override
    public List<IssueCommentDTOResponse> getIssueComments(String owner, String repo, int pullNumber) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}/issues/{pullNumber}/comments", owner, repo, pullNumber)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<IssueCommentDTOResponse>>() {
            }).block();

    }

    @Override
    public List<PullReviewDTOResponse> getPullReviews(String owner, String repo, int pullNumber) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}/pulls/{pullNumber}/reviews", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<List<PullReviewDTOResponse>>() {
            }).block();
    }

    @Override
    public List<PullCommentDTOResponse> getPullComments(String owner, String repo, int pullNumber) {
        return webClient.get()
            .uri("repos/{owner}/{repo}/pulls/{pullNumber}/comments", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<List<PullCommentDTOResponse>>() {
            }).block();

    }

    @Override
    public List<PullCommitDTOResponse> getAllCommitsInPullRequest(String owner, String repo, int pullNumber) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}/pulls/{pullNumber}/commits", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<List<PullCommitDTOResponse>>() {
            }).block();
    }
}
