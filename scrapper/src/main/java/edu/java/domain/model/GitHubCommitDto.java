package edu.java.domain.model;

import edu.java.model.github.dto.PullCommitDtoResponse;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GitHubCommitDto {
    private Long commitId;
    private Long linkId;
    private String sha;
    private String author;
    private OffsetDateTime createdAt;
    private String message;

    public static GitHubCommitDto create(PullCommitDtoResponse pullCommitDTOResponse) {
        GitHubCommitDto commitDTO = new GitHubCommitDto();
        commitDTO.setSha(pullCommitDTOResponse.getSha());
        commitDTO.setAuthor(pullCommitDTOResponse.getCommit().getCommitter().name());
        commitDTO.setCreatedAt(pullCommitDTOResponse.getCommit().getCommitter().date()
            .withOffsetSameInstant(ZoneOffset.UTC));
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
        GitHubCommitDto commitDTO = (GitHubCommitDto) o;
        return Objects.equals(linkId, commitDTO.linkId) && Objects.equals(sha, commitDTO.sha)
            && Objects.equals(author, commitDTO.author) && Objects.equals(createdAt, commitDTO.createdAt)
            && Objects.equals(message, commitDTO.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(linkId, sha, author, createdAt, message);
    }
}
