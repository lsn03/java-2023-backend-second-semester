package edu.java.domain.repository;

import edu.java.domain.model.GitHubCommitDTO;
import java.net.URI;
import java.util.List;

public interface GitHubRepository {
    Integer addCommits(List<GitHubCommitDTO> gitHubCommitList);

    Integer deleteCommits(List<GitHubCommitDTO> gitHubCommitList);

    List<GitHubCommitDTO> getCommits(Long linkId);

    List<GitHubCommitDTO> getCommits(URI uri);

}