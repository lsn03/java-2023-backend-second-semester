package edu.java.domain.repository.jpa.mapper;

import edu.java.domain.model.GitHubCommitDTO;
import edu.java.domain.repository.jpa.entity.GitHubCommitEntity;
import java.time.ZoneOffset;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapperGitHubCommitDTOGitHubCommitEntity {
    public static GitHubCommitEntity dtoToEntity(GitHubCommitDTO dto) {
        return new GitHubCommitEntity(
            dto.getCommitId(),
            dto.getLinkId(),
            dto.getSha(),
            dto.getAuthor(),
            dto.getCreatedAt().toLocalDateTime(),
            dto.getMessage(),
            null
        );
    }

    public static GitHubCommitDTO dtoToEntity(GitHubCommitEntity entity) {
        return new GitHubCommitDTO(
            entity.getCommitId(),
            entity.getLinkId(),
            entity.getSha(),
            entity.getAuthor(),
            entity.getCreatedAt().atOffset(ZoneOffset.UTC),
            entity.getMessage()
        );
    }
}
