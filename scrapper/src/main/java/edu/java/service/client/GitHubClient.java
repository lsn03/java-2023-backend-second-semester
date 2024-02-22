package edu.java.service.client;

import edu.java.model.github.PullRequestModelResponse;
import edu.java.model.github.dto.IssueCommentDTOResponse;
import edu.java.model.github.dto.PullCommentDTOResponse;
import edu.java.model.github.dto.PullCommitDTOResponse;
import edu.java.model.github.dto.PullReviewDTOResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface GitHubClient {
    Mono<PullRequestModelResponse> fetchPullRequest(String owner, String name, int pullNumber);

    Mono<List<IssueCommentDTOResponse>> getIssueComments(String owner, String repo, int pullNumber);

    Mono<List<PullReviewDTOResponse>> getPullReviews(String owner, String repo, int pullNumber);

    Mono<List<PullCommentDTOResponse>> getPullComments(String owner, String repo, int pullNumber);

    Mono<List<PullCommitDTOResponse>> getAllCommitsInPullRequest(String owner, String repo, int pullNumber);
}
