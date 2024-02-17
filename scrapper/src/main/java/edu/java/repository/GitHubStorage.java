package edu.java.repository;

import edu.java.model.github.PullRequestModel;

public interface GitHubStorage {
    void saveGitHubData(String key, PullRequestModel model);

    PullRequestModel getGitHubData(String key);
}
