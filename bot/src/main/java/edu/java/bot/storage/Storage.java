package edu.java.bot.storage;

import edu.java.bot.processor.UserState;
import java.util.List;

public interface Storage {
    boolean addUrl(Long userId, String url);

    boolean removeUrl(Long userId, String url);

    void authUser(Long userId);

    boolean isUserAuth(Long userId);

    List<String> getUserTracks(Long userId);

    UserState getUserState(Long id);

    UserState setUserState(Long id, UserState state);
}
