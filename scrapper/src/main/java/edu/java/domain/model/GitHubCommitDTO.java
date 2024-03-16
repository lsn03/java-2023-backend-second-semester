package edu.java.domain.model;

import edu.java.model.github.dto.PullCommitDTOResponse;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        commitDTO.setCreatedAt(pullCommitDTOResponse.getCommit().getCommitter().date().withOffsetSameInstant(ZoneOffset.UTC));
        commitDTO.setMessage(pullCommitDTOResponse.getCommit().getMessage());
        return commitDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GitHubCommitDTO commitDTO = (GitHubCommitDTO) o;
        return Objects.equals(linkId, commitDTO.linkId) && Objects.equals(sha, commitDTO.sha)
            && Objects.equals(author, commitDTO.author) && Objects.equals(createdAt, commitDTO.createdAt)
            && Objects.equals(message, commitDTO.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(linkId, sha, author, createdAt, message);
    }
}
