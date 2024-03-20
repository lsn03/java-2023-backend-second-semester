package edu.java.bot.storage;

import edu.java.bot.model.dto.response.ApiErrorResponse;
import edu.java.bot.model.dto.response.LinkResponse;
import edu.java.bot.processor.UserState;
import java.util.List;
import java.util.Optional;

public interface Storage {
    Optional<ApiErrorResponse> addUrl(Long userId, String url);

    Optional<ApiErrorResponse> removeUrl(Long userId, String url);

    void authUser(Long userId);

    boolean isUserAuth(Long userId);

    List<LinkResponse> getUserTracks(Long userId);

    UserState getUserState(Long id);

    void setUserState(Long id, UserState state);
}
