package edu.java.domain.repository;

import edu.java.domain.model.ChatDto;
import java.util.List;

public interface ChatRepository {

    void add(Long tgChatId);

    void remove(Long tgChatId);

    List<ChatDto> findAll();
}
