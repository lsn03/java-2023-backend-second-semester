package edu.java.service;

import edu.java.model.github.PullRequestModel;
import edu.java.model.github.dto.IssueCommentDTO;
import edu.java.model.github.dto.PullCommentDTO;
import edu.java.model.github.dto.PullCommitDTO;
import edu.java.model.github.dto.PullReviewDTO;
import java.util.List;
import edu.java.repository.GitHubStorage;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class GitHubService {

    private final WebClient webClient;

    public PullRequestModel fetchRepoData(String owner, String repo, int pullNumber){
        PullRequestModel model = new PullRequestModel();
        model.setIssueCommentDTOS(getIssueComments(owner, repo, pullNumber).block());
        model.setPullCommentDTOS(getPullComments(owner, repo, pullNumber).block());
        model.setPullCommitDTOS(getAllCommitsInPullRequest(owner, repo, pullNumber).block());
        model.setPullReviewDTOS(getPullReviews(owner, repo, pullNumber).block());


        return model;
    }
    public Mono<List<IssueCommentDTO>> getIssueComments(String owner, String repo, int pullNumber) {
        return webClient.get().uri("/repos/{owner}/{repo}/issues/{pullNumber}/comments", owner, repo, pullNumber)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<>() {
            });

    }

    public Mono<List<PullReviewDTO>> getPullReviews(String owner, String repo, int pullNumber) {
        return webClient.get().uri("/repos/{owner}/{repo}/pulls/{pullNumber}/reviews", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
            });
    }

    public Mono<List<PullCommentDTO>> getPullComments(
        String owner, String repo, int pullNumber
    ) {

        return webClient.get().uri("repos/{owner}/{repo}/pulls/{pullNumber}/comments", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
            });

    }


    public Mono<List<PullCommitDTO>> getAllCommitsInPullRequest(String owner, String repo, int pullNumber) {
        return webClient.get().uri("/repos/{owner}/{repo}/pulls/{pullNumber}/commits", owner, repo, pullNumber)
            .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
            });
    }
}
