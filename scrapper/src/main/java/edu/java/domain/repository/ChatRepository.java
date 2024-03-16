package edu.java.domain.repository;

import edu.java.domain.model.ChatDTO;
import java.util.List;

public interface ChatRepository {

    void add(Long tgChatId);

    void remove(Long tgChatId);

    List<ChatDTO> findAll();
}
