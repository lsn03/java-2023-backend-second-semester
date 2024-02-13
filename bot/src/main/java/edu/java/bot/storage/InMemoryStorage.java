package edu.java.bot.storage;

import edu.java.bot.processor.UserState;
import edu.java.bot.service.LinkParserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStorage implements Storage {
    private final Map<Long, List<String>> trackedUrls;
    private final Map<Long, Boolean> authUser;
    private final Map<Long, UserState> userStateMap;
    private final LinkParserService parserService;

    @Autowired
    public InMemoryStorage(LinkParserService parserService) {
        this.parserService = parserService;
        this.trackedUrls = new HashMap<>();
        this.authUser = new HashMap<>();
        this.userStateMap = new HashMap<>();
    }

    @Override
    public void authUser(Long userId) {

        if (!isUserAuth(userId)) {
            setUserState(userId, UserState.DEFAULT);
            authUser.put(userId, true);
        }

    }

    @Override
    public boolean isUserAuth(Long userId) {
        return authUser.getOrDefault(userId, false);
    }

    @Override
    public boolean addUrl(Long userId, String url) {
        parserService.process(url);

        if (isUserAuth(userId)) {
            List<String> urls = trackedUrls.computeIfAbsent(userId, k -> new ArrayList<>());
            if (urls.contains(url)) {
                return false;
            } else {
                urls.add(url);
                return true;
            }
        } else {
            return false;
        }

    }

    @Override
    public boolean removeUrl(Long userId, String url) {
        if (!isUserAuth(userId)) {
            return false;
        }
        List<String> urls = trackedUrls.get(userId);
        if (urls == null) {
            return false;
        }

        parserService.process(url);
        if (urls.contains(url)) {
            urls.remove(url);
            if (urls.isEmpty()) {
                trackedUrls.remove(userId);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> getUserTracks(Long userId) {
        return new ArrayList<>(trackedUrls.getOrDefault(userId, List.of()));
    }

    @Override
    public UserState getUserState(Long id) {
        return userStateMap.getOrDefault(id, UserState.UNAUTHORIZED);
    }

    @Override
    public void setUserState(Long id, UserState state) {
        userStateMap.put(id, state);
    }
}
