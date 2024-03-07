package edu.java.domain.repository;

import edu.java.domain.model.LinkDTO;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.List;

public interface LinkRepository {
    LinkDTO add(LinkDTO linkDTO);

    Integer remove(LinkDTO linkDTO);

    List<LinkDTO> findAll(Long tgChatId);

    Long findUrl(URI uri);

    @Transactional void updateLink(LinkDTO elem);

    @Transactional List<LinkDTO> findAllOldLinks(Integer time, String timeUnit);
}
