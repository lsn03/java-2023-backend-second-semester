package edu.java.service.process;

import edu.java.domain.model.LinkDTO;
import java.util.List;

public interface LinkService {
    LinkDTO add(LinkDTO linkDTO);

    Integer remove(LinkDTO linkDTO);

    List<LinkDTO> findAll(Long tgChatId);
}
