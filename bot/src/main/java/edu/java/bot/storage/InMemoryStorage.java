package edu.java.bot.storage;

import edu.java.bot.exception.ListEmptyException;
import edu.java.bot.model.dto.request.AddLinkRequest;
import edu.java.bot.model.dto.request.RemoveLinkRequest;
import edu.java.bot.model.dto.response.ApiErrorResponse;
import edu.java.bot.model.dto.response.LinkResponse;
import edu.java.bot.model.dto.response.ListLinksResponse;
import edu.java.bot.model.dto.response.MyResponse;
import edu.java.bot.processor.UserState;
import edu.java.bot.service.LinkParserService;
import edu.java.bot.service.client.ScrapperHttpClient;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStorage implements Storage {
    private final Set<Long> users;
    private final Map<Long, UserState> userStateMap;
    private final LinkParserService parserService;
    private final ScrapperHttpClient scrapperHttpClient;

    @Autowired
    public InMemoryStorage(LinkParserService parserService, ScrapperHttpClient scrapperHttpClient) {
        this.parserService = parserService;
        this.users = new HashSet<>();
        this.userStateMap = new HashMap<>();
        this.scrapperHttpClient = scrapperHttpClient;
    }

    @Override
    public void authUser(Long userId) {

        if (!isUserAuth(userId)) {
            setUserState(userId, UserState.DEFAULT);

            scrapperHttpClient.makeChat(userId);
            users.add(userId);
        }

    }

    @Override
    public boolean isUserAuth(Long userId) {
        return users.contains(userId);
    }

    @Override
    public Optional<ApiErrorResponse> addUrl(Long userId, String url) {
        parserService.process(url);
        MyResponse response = scrapperHttpClient.trackLink(new AddLinkRequest(url), userId);

        if (response instanceof ApiErrorResponse) {
            return Optional.of((ApiErrorResponse) response);
        }

        return Optional.empty();

    }

    @Override
    public Optional<ApiErrorResponse> removeUrl(Long userId, String url) {

        MyResponse urls = scrapperHttpClient.unTrackLink(new RemoveLinkRequest(url), userId);
        if (urls instanceof LinkResponse) {
            return Optional.empty();
        }

        ApiErrorResponse response = (ApiErrorResponse) urls;
        return Optional.of(response);
    }

    @Override
    public List<LinkResponse> getUserTracks(Long userId) {
        MyResponse urls = scrapperHttpClient.getLinks(userId);
        if (urls instanceof ListLinksResponse urls2) {
            return urls2.getLists();
        }
        ApiErrorResponse response = (ApiErrorResponse) urls;
        if (response.getExceptionName().equals(ListEmptyException.class.getSimpleName())) {
            throw new ListEmptyException(response.getExceptionMessage());
        }
        return List.of();
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
