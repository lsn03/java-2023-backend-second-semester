package edu.java.domain.model;

import edu.java.model.github.dto.PullCommitDTOResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GitHubCommitDTO {
    private Long commitId;
    private Long linkId;
    private String sha;
    private String author;
    private OffsetDateTime createdAt;
    private String message;

    public static GitHubCommitDTO create(PullCommitDTOResponse pullCommitDTOResponse) {
        GitHubCommitDTO commitDTO = new GitHubCommitDTO();
        commitDTO.setSha(pullCommitDTOResponse.getSha());
        commitDTO.setAuthor(pullCommitDTOResponse.getCommit().getCommitter().name());
        commitDTO.setCreatedAt(pullCommitDTOResponse.getCommit().getCommitter().date());
        commitDTO.setMessage(pullCommitDTOResponse.getCommit().getMessage());
        return commitDTO;
    }
}
