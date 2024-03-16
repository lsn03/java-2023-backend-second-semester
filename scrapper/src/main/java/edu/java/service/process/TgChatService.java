package edu.java.service.process;

import edu.java.domain.model.ChatDTO;
import java.util.List;

public interface TgChatService {
    void add(Long tgChatId);

    void remove(Long tgChatId);

    List<ChatDTO> findAll();
}
