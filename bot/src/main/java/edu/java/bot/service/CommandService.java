package edu.java.bot.service;

import edu.java.bot.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandService {
    private final Storage storage;
    private final LinkParserService parserService;

    @Autowired
    public CommandService(Storage storage, LinkParserService parserService) {
        this.storage = storage;
        this.parserService = parserService;
    }

    public List<String> getUserTracks(Long userId) {
        return storage.getUserTracks(userId);
    }

    public boolean addTrack(Long userId, String url) {
        if (parserService.process(url)) {
            return storage.addUrl(userId, url);
        }
        return false;
    }

    public boolean removeTrack(Long userId, String url) {
        if (parserService.process(url)) {
            return storage.removeUrl(userId, url);
        }
        return false;
    }

    public void authorizeUser(Long chatId) {
        storage.authUser(chatId);
    }
}
