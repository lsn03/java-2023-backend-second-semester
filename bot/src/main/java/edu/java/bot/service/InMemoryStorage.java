package edu.java.bot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStorage {
    private final Map<Long, List<String>> trackedUrls;

    public InMemoryStorage() {
        this.trackedUrls = new ConcurrentHashMap<>();
    }

    public boolean addUrl(Long userId, String url) {
        List<String> urls = trackedUrls.computeIfAbsent(userId, k -> new ArrayList<>());
        if (urls.contains(url)) {
            return false;
        } else {
            urls.add(url);
            return true;
        }

    }

    public boolean removeUrl(Long userId, String url) {
        List<String> urls = trackedUrls.get(userId);
        if (urls != null) {
            urls.remove(url);
            if(urls.isEmpty()){
                trackedUrls.remove(userId);
            }
            return true;
        } else {
            return false;
        }

    }
    public List<String> getUserTracks(Long userId){
        return new ArrayList<>(trackedUrls.get(userId));
    }
}
