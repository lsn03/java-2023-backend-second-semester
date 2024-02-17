package edu.java.model.github;

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
    private List<IssueCommentDTO> issueCommentDTOS;
    private List<PullCommentDTO> pullCommentDTOS;
    private List<PullCommitDTO> pullCommitDTOS;
    private List<PullReviewDTO> pullReviewDTOS;

}
