package edu.java.service.client;

import edu.java.model.github.PullRequestModelResponse;
import edu.java.model.github.dto.IssueCommentDTOResponse;
import edu.java.model.github.dto.PullCommentDTOResponse;
import edu.java.model.github.dto.PullCommitDTOResponse;
import edu.java.model.github.dto.PullReviewDTOResponse;
import java.util.List;

public interface GitHubClient {
    PullRequestModelResponse fetchPullRequest(String owner, String name, int pullNumber);

    List<IssueCommentDTOResponse> getIssueComments(String owner, String repo, int pullNumber);

    List<PullReviewDTOResponse> getPullReviews(String owner, String repo, int pullNumber);

    List<PullCommentDTOResponse> getPullComments(String owner, String repo, int pullNumber);

    List<PullCommitDTOResponse> getAllCommitsInPullRequest(String owner, String repo, int pullNumber);
}
