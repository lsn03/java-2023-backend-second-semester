package edu.java.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.model.github.dto.IssueCommentDtoResponse;
import edu.java.model.github.dto.PullCommentDtoResponse;
import edu.java.model.github.dto.PullCommitDtoResponse;
import edu.java.model.github.dto.PullReviewDtoResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PullRequestModelResponse {
    @JsonProperty("issue_comment_dto")
    private List<IssueCommentDtoResponse> issueCommentDTOS;
    @JsonProperty("pull_comment_dto")
    private List<PullCommentDtoResponse> pullCommentDTOS;
    @JsonProperty("pull_commit_dto")
    private List<PullCommitDtoResponse> pullCommitDTOS;
    @JsonProperty("pull_review_dto")
    private List<PullReviewDtoResponse> pullReviewDTOS;

}
