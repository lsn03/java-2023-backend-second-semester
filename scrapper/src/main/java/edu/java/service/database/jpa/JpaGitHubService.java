package edu.java.service.database.jpa;

import edu.java.domain.model.GitHubCommitDto;
import edu.java.domain.repository.jpa.JpaGitHubRepository;
import edu.java.exception.exception.LinkNotFoundException;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.service.database.GitHubService;
import jakarta.persistence.EntityExistsException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaGitHubService implements GitHubService {
    private final JpaGitHubRepository jpaGitHubRepository;

    @Override
    @Transactional
    public Integer addCommits(List<GitHubCommitDto> gitHubCommitList) {
        try {
            return jpaGitHubRepository.addCommits(gitHubCommitList);
        } catch (EntityExistsException e) {
            throw new RecordAlreadyExistException(e);
        } catch (ConstraintViolationException e) {
            throw new LinkNotFoundException(e);
        }
    }

    @Override
    @Transactional
    public Integer deleteCommits(List<GitHubCommitDto> gitHubCommitList) {
        return jpaGitHubRepository.deleteCommits(gitHubCommitList);
    }

    @Override
    public List<GitHubCommitDto> getCommits(Long linkId) {
        return jpaGitHubRepository.getCommits(linkId);
    }

    @Override
    public List<GitHubCommitDto> getCommits(URI uri) {
        return jpaGitHubRepository.getCommits(uri);
    }
}
