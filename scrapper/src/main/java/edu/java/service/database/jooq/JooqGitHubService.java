package edu.java.service.database.jooq;

import edu.java.domain.model.GitHubCommitDto;
import edu.java.domain.repository.jooq.JooqGitHubRepository;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.service.database.GitHubService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Primary
public class JooqGitHubService implements GitHubService {
    private final JooqGitHubRepository jooqGitHubRepository;

    @Override
    @Transactional
    public Integer addCommits(List<GitHubCommitDto> gitHubCommitList) {
        try {
            return jooqGitHubRepository.addCommits(gitHubCommitList);
        } catch (DuplicateKeyException e) {
            throw new RecordAlreadyExistException(e);
        }
    }

    @Override
    @Transactional
    public Integer deleteCommits(List<GitHubCommitDto> gitHubCommitList) {
        return jooqGitHubRepository.deleteCommits(gitHubCommitList);
    }

    @Override
    public List<GitHubCommitDto> getCommits(Long linkId) {
        return jooqGitHubRepository.getCommits(linkId);
    }

    @Override
    public List<GitHubCommitDto> getCommits(URI uri) {
        return jooqGitHubRepository.getCommits(uri);
    }
}
