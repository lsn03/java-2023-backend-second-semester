package edu.java.service.database;

import edu.java.domain.model.ChatDTO;
import java.util.List;

public interface TgChatService {
    void add(Long tgChatId);

    void remove(Long tgChatId);

    List<ChatDTO> findAll();
}
