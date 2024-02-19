package edu.java.service.client;

import edu.java.model.github.PullRequestModel;
import edu.java.model.github.dto.IssueCommentDTO;
import edu.java.model.github.dto.PullCommentDTO;
import edu.java.model.github.dto.PullCommitDTO;
import edu.java.model.github.dto.PullReviewDTO;
import java.util.List;
import reactor.core.publisher.Mono;

public interface GitHubClient {
    Mono<PullRequestModel> fetchPullRequest(String owner, String name, int pullNumber);

    Mono<List<IssueCommentDTO>> getIssueComments(String owner, String repo, int pullNumber);

    Mono<List<PullReviewDTO>> getPullReviews(String owner, String repo, int pullNumber);

    Mono<List<PullCommentDTO>> getPullComments(String owner, String repo, int pullNumber);

    Mono<List<PullCommitDTO>> getAllCommitsInPullRequest(String owner, String repo, int pullNumber);
}
