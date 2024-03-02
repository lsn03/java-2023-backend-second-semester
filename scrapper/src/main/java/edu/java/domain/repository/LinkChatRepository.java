package edu.java.domain.repository;

import edu.java.domain.model.LinkChatDTO;
import java.util.List;

public interface LinkChatRepository {
    void add(LinkChatDTO linkDTO);

    void remove(LinkChatDTO linkDTO);

    void remove(Long chatId, Long linkId);

    List<LinkChatDTO> findAll();
}
