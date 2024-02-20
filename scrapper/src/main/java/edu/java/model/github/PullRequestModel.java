package edu.java.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.model.github.dto.IssueCommentDTO;
import edu.java.model.github.dto.PullCommentDTO;
import edu.java.model.github.dto.PullCommitDTO;
import edu.java.model.github.dto.PullReviewDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PullRequestModel {
    @JsonProperty("issue_comment_dto")
    private List<IssueCommentDTO> issueCommentDTOS;
    @JsonProperty("pull_comment_dto")
    private List<PullCommentDTO> pullCommentDTOS;
    @JsonProperty("pull_commit_dto")
    private List<PullCommitDTO> pullCommitDTOS;
    @JsonProperty("pull_review_dto")
    private List<PullReviewDTO> pullReviewDTOS;

}
