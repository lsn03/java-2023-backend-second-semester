package edu.java.domain.repository;

import edu.java.domain.model.GitHubCommitDto;
import java.net.URI;
import java.util.List;

public interface GitHubRepository {
    Integer addCommits(List<GitHubCommitDto> gitHubCommitList);

    Integer deleteCommits(List<GitHubCommitDto> gitHubCommitList);

    List<GitHubCommitDto> getCommits(Long linkId);

    List<GitHubCommitDto> getCommits(URI uri);

}
