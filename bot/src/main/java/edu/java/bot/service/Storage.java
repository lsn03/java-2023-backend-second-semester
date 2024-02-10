package edu.java.bot.service;

import java.util.List;

public interface Storage {
    boolean addUrl(Long userId, String url);

    boolean removeUrl(Long userId, String url);
    void authUser(Long userId);
    List<String> getUserTracks(Long userId);
}
