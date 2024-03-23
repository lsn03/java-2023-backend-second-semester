package edu.java.service.database.jpa;

import edu.java.domain.model.GitHubCommitDTO;
import edu.java.domain.repository.jpa.JpaGitHubRepository;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.service.database.GitHubService;
import jakarta.persistence.EntityExistsException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Primary
public class JpaGitHubService implements GitHubService {
    private final JpaGitHubRepository jpaGitHubRepository;

    @Override
    @Transactional
    public Integer addCommits(List<GitHubCommitDTO> gitHubCommitList) {
        try {
            return jpaGitHubRepository.addCommits(gitHubCommitList);
        } catch (EntityExistsException e) {
            throw new RecordAlreadyExistException(e);
        }
    }

    @Override
    @Transactional
    public Integer deleteCommits(List<GitHubCommitDTO> gitHubCommitList) {
        return jpaGitHubRepository.deleteCommits(gitHubCommitList);
    }

    @Override
    @Transactional
    public List<GitHubCommitDTO> getCommits(Long linkId) {
        return jpaGitHubRepository.getCommits(linkId);
    }

    @Override
    @Transactional
    public List<GitHubCommitDTO> getCommits(URI uri) {
        return jpaGitHubRepository.getCommits(uri);
    }
}