package edu.java.domain.repository;

import edu.java.domain.model.LinkDTO;
import java.net.URI;
import java.util.List;

public interface LinkRepository {
    LinkDTO add(LinkDTO linkDTO);

    Integer remove(LinkDTO linkDTO);

    List<LinkDTO> findAllByChatId(Long tgChatId);

    List<LinkDTO> findAllByLinkId(Long linkId);

    Long findLinkIdByUrl(URI uri);

    List<LinkDTO> findAll();

    void updateLink(LinkDTO elem);

    List<LinkDTO> findAllOldLinks(Integer timeInSeconds);
}
