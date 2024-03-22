package edu.java.domain.repository.jpa;

import edu.java.domain.repository.jpa.entity.GitHubCommitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGitHubRepositoryInterface extends JpaRepository<GitHubCommitEntity, Long> {

}
