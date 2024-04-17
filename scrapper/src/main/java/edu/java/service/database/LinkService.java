package edu.java.service.database;

import edu.java.domain.model.LinkDto;
import java.util.List;

public interface LinkService {
    LinkDto add(LinkDto linkDTO);

    Integer remove(LinkDto linkDTO);

    List<LinkDto> findAll(Long tgChatId);
}
