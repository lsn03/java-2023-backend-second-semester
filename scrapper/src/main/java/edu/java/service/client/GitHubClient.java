package edu.java.service.client;

import edu.java.model.github.PullRequestModelResponse;
import edu.java.model.github.dto.IssueCommentDtoResponse;
import edu.java.model.github.dto.PullCommentDtoResponse;
import edu.java.model.github.dto.PullCommitDtoResponse;
import edu.java.model.github.dto.PullReviewDtoResponse;
import java.util.List;

public interface GitHubClient {
    PullRequestModelResponse fetchPullRequest(String owner, String name, int pullNumber);

    List<IssueCommentDtoResponse> getIssueComments(String owner, String repo, int pullNumber);

    List<PullReviewDtoResponse> getPullReviews(String owner, String repo, int pullNumber);

    List<PullCommentDtoResponse> getPullComments(String owner, String repo, int pullNumber);

    List<PullCommitDtoResponse> getAllCommitsInPullRequest(String owner, String repo, int pullNumber);
}
