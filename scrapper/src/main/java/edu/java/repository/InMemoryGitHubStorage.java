package edu.java.repository;

import edu.java.model.github.PullRequestModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryGitHubStorage implements GitHubStorage {
    private final Map<String, PullRequestModel> data;

    private final Map<String, Set<Long>> urlToUsers;

    private final Map<Long, Set<String>> userToUrls;

    public InMemoryGitHubStorage() {
        data = new HashMap<>();
        urlToUsers = new HashMap<>();
        userToUrls = new HashMap<>();
    }

    public void trackUrl(Long userId, String url) {
        userToUrls.computeIfAbsent(userId, k -> new HashSet<>()).add(url);
        urlToUsers.computeIfAbsent(url, k -> new HashSet<>()).add(userId);
    }

    public boolean isUrlTrackedByUser(Long userId, String url) {
        return userToUrls.getOrDefault(userId, Collections.emptySet()).contains(url);
    }

    public Set<Long> getUsersTrackingUrl(String url) {
        return urlToUsers.getOrDefault(url, Collections.emptySet());
    }

    @Override
    public void saveGitHubData(String key, PullRequestModel model) {
        data.put(key, model);
    }

    @Override
    public PullRequestModel getGitHubData(String key) {
        return data.get(key);
    }
}
