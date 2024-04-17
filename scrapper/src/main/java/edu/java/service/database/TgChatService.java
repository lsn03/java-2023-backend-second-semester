package edu.java.service.database;

import edu.java.domain.model.ChatDto;
import java.util.List;

public interface TgChatService {
    void add(Long tgChatId);

    void remove(Long tgChatId);

    List<ChatDto> findAll();
}
