package edu.java.domain.repository.jpa;

import edu.java.domain.repository.jpa.entity.GitHubCommitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaGitHubRepositoryInterface extends JpaRepository<GitHubCommitEntity, Long> {
    List<GitHubCommitEntity> findAllByLinkEntity_LinkId(Long linkId);
    List<GitHubCommitEntity> findAllByLinkEntityUri(String uri);
}
