package edu.java.domain.repository;

import edu.java.domain.model.LinkDTO;
import java.util.List;

public interface LinkRepository {
    LinkDTO add(LinkDTO linkDTO);

    Integer remove(LinkDTO linkDTO);

    List<LinkDTO> findAll(Long tgChatId);
}
