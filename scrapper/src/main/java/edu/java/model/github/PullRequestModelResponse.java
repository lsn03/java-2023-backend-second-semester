package edu.java.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.model.github.dto.IssueCommentDTOResponse;
import edu.java.model.github.dto.PullCommentDTOResponse;
import edu.java.model.github.dto.PullCommitDTOResponse;
import edu.java.model.github.dto.PullReviewDTOResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PullRequestModelResponse {
    @JsonProperty("issue_comment_dto")
    private List<IssueCommentDTOResponse> issueCommentDTOS;
    @JsonProperty("pull_comment_dto")
    private List<PullCommentDTOResponse> pullCommentDTOS;
    @JsonProperty("pull_commit_dto")
    private List<PullCommitDTOResponse> pullCommitDTOS;
    @JsonProperty("pull_review_dto")
    private List<PullReviewDTOResponse> pullReviewDTOS;

}
