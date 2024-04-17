package edu.java.domain.repository;

import edu.java.domain.model.LinkDto;
import java.util.List;

public interface LinkChatRepository {
    void add(LinkDto linkDTO);

    Integer remove(LinkDto linkDTO);

    Integer remove(Long tgChatId);

    List<LinkDto> findAllByChatId(Long tgChatId);

    List<LinkDto> findAllByLinkId(Long tgChatId);
}
