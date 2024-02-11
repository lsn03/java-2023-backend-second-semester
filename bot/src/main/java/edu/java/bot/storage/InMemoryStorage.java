package edu.java.bot.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStorage implements Storage {
    private final Map<Long, List<String>> trackedUrls;
    private final Map<Long, Boolean> authUser;

    public InMemoryStorage() {
        this.trackedUrls = new HashMap<>();
        this.authUser = new HashMap<>();
    }

    @Override
    public void authUser(Long userId) {
        var result = authUser.getOrDefault(userId, false);
        if (!result) {
            authUser.put(userId, true);
        }

    }

    @Override
    public boolean addUrl(Long userId, String url) {
        List<String> urls = trackedUrls.computeIfAbsent(userId, k -> new ArrayList<>());
        if (urls.contains(url)) {
            return false;
        } else {
            urls.add(url);
            return true;
        }

    }

    @Override
    public boolean removeUrl(Long userId, String url) {
        List<String> urls = trackedUrls.get(userId);
        if (urls != null) {
            if (urls.contains(url)) {
                urls.remove(url);
                if (urls.isEmpty()) {
                    trackedUrls.remove(userId);
                }
                return true;
            }
        }
        return false;

    }

    @Override
    public List<String> getUserTracks(Long userId) {
        return new ArrayList<>(trackedUrls.getOrDefault(userId, List.of()));
    }
}
