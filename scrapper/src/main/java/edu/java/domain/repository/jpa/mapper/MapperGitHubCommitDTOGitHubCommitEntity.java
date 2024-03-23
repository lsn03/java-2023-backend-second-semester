package edu.java.domain.repository.jpa.mapper;

import edu.java.domain.model.GitHubCommitDTO;
import edu.java.domain.repository.jpa.entity.GitHubCommitEntity;
import edu.java.domain.repository.jpa.entity.LinkEntity;
import java.time.ZoneOffset;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapperGitHubCommitDTOGitHubCommitEntity {
    public static GitHubCommitEntity dtoToEntity(GitHubCommitDTO dto) {
        var entity = new LinkEntity();
        entity.setLinkId(dto.getLinkId());
        return new GitHubCommitEntity(
            dto.getCommitId(),
            dto.getSha(),
            dto.getAuthor(),
            dto.getCreatedAt().toLocalDateTime(),
            dto.getMessage(),
            entity
        );
    }

    public static GitHubCommitDTO entityToDto(GitHubCommitEntity entity) {
        return new GitHubCommitDTO(
            entity.getCommitId(),
            entity.getLinkEntity().getLinkId(),
            entity.getSha(),
            entity.getAuthor(),
            entity.getCreatedAt().atOffset(ZoneOffset.UTC),
            entity.getMessage()
        );
    }
}
