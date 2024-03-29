package edu.java.bot.storage;

import edu.java.bot.exception.ApiErrorException;
import edu.java.bot.exception.IncorrectParametersException;
import edu.java.bot.exception.ListEmptyException;
import edu.java.bot.exception.UserAlreadyExistException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
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

            try {
                scrapperHttpClient.makeChat(userId);
            } catch (ApiErrorException e) {
                if (!e.getErrorResponse().getExceptionName().equals(UserAlreadyExistException.class.getSimpleName())) {
                    throw new RuntimeException(e);
                }
                users.add(userId);
            } catch (Exception e) {
                log.error("{} {}", e.getMessage(), e.getStackTrace());
                throw new RuntimeException(e);
            }

        }

    }

    @Override
    public boolean isUserAuth(Long userId) {
        return users.contains(userId);
    }

    @Override
    public Optional<ApiErrorResponse> addUrl(Long userId, String url) {
        parserService.process(url);
        try {
            scrapperHttpClient.trackLink(new AddLinkRequest(url), userId);
        } catch (ApiErrorException e) {
            return Optional.of(e.getErrorResponse());
        } catch (Exception e) {

        }

        return Optional.empty();

    }

    @Override
    public Optional<ApiErrorResponse> removeUrl(Long userId, String url) {

        try {
            scrapperHttpClient.unTrackLink(new RemoveLinkRequest(url), userId);
        } catch (ApiErrorException e) {
            return Optional.of(e.getErrorResponse());
        }

        return Optional.empty();

    }

    @Override
    public List<LinkResponse> getUserTracks(Long userId) {
        MyResponse urls;
        try {
            urls = scrapperHttpClient.getLinks(userId);
            if (urls instanceof ListLinksResponse urls2) {
                return urls2.getLists();
            }
        } catch (ApiErrorException e) {
            if (!e.getErrorResponse().getExceptionName().equals(ListEmptyException.class.getSimpleName())) {
                throw new IncorrectParametersException(e.getErrorResponse().getExceptionMessage());
            }
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
