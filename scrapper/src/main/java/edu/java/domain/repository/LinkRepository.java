package edu.java.domain.repository;

import edu.java.domain.model.LinkDto;
import java.net.URI;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface LinkRepository {
    LinkDto add(LinkDto linkDTO);

    Integer remove(LinkDto linkDTO);

    List<LinkDto> findAllByChatId(Long tgChatId);

    Long findUrl(URI uri);

    @Transactional List<LinkDto> findAll();

    void updateLink(LinkDto elem);

    List<LinkDto> findAllOldLinks(Integer timeInSeconds);
}
