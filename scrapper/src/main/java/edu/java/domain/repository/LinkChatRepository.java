package edu.java.domain.repository;

import edu.java.domain.model.LinkDTO;

import java.util.List;

public interface LinkChatRepository {
    void add(LinkDTO linkDTO);

    Integer remove(LinkDTO linkDTO);

    Integer remove(Long tgChatId);

    List<LinkDTO> findAllByChatId(Long tgChatId);

    List<LinkDTO> findAllByLinkId(Long tgChatId);
}
