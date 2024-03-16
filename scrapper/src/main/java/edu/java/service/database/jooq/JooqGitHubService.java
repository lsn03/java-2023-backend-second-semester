package edu.java.service.database.jooq;

import edu.java.domain.model.GitHubCommitDTO;
import edu.java.domain.repository.jooq.JooqGitHubRepository;
import edu.java.service.database.GitHubService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class JooqGitHubService implements GitHubService {
    private final JooqGitHubRepository jooqGitHubRepository;

    @Override
    public Integer addCommits(List<GitHubCommitDTO> gitHubCommitList) {
        return jooqGitHubRepository.addCommits(gitHubCommitList);
    }

    @Override
    public Integer deleteCommits(List<GitHubCommitDTO> gitHubCommitList) {
        return jooqGitHubRepository.deleteCommits(gitHubCommitList);
    }

    @Override
    public List<GitHubCommitDTO> getCommits(Long linkId) {
        return jooqGitHubRepository.getCommits(linkId);
    }

    @Override
    public List<GitHubCommitDTO> getCommits(URI uri) {
        return jooqGitHubRepository.getCommits(uri);
    }
}
