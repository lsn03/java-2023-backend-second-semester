package edu.java.service.process;

import edu.java.domain.model.LinkDTO;
import java.util.Collection;

public interface LinkService {
    LinkDTO add(LinkDTO linkDTO);

    Integer remove(LinkDTO linkDTO);

    Collection<LinkDTO> findAll(Long tgChatId);
}
