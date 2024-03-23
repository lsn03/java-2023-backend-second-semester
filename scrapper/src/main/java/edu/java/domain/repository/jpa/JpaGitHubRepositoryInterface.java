package edu.java.domain.repository.jpa;

import edu.java.domain.repository.jpa.entity.GitHubCommitEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGitHubRepositoryInterface extends JpaRepository<GitHubCommitEntity, Long> {
    List<GitHubCommitEntity> findAllByLinkEntityLinkId(Long linkId);

    List<GitHubCommitEntity> findAllByLinkEntityUri(String uri);
}
