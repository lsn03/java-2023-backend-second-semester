package edu.java.domain.repository;

import edu.java.domain.model.LinkDto;
import java.net.URI;
import java.util.List;

public interface LinkRepository {
    LinkDto add(LinkDto linkDTO);

    Integer remove(LinkDto linkDTO);

    List<LinkDto> findAllByChatId(Long tgChatId);

    List<LinkDto> findAllByLinkId(Long linkId);

    Long findLinkIdByUrl(URI uri);

    List<LinkDto> findAll();

    void updateLink(LinkDto elem);

    List<LinkDto> findAllOldLinks(Integer timeInSeconds);
}
