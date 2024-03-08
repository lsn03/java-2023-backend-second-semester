package edu.java.domain.repository;

import edu.java.domain.model.LinkDTO;
import java.net.URI;
import java.util.List;

public interface LinkRepository {
    LinkDTO add(LinkDTO linkDTO);

    Integer remove(LinkDTO linkDTO);

    List<LinkDTO> findAll(Long tgChatId);

    Long findUrl(URI uri);

    void updateLink(LinkDTO elem);

    List<LinkDTO> findAllOldLinks(Integer time, String timeUnit);
}
