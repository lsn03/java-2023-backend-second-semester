package edu.java.service.process.jooq;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.jooq.JooqLinkChatRepository;
import edu.java.domain.repository.jooq.JooqLinkRepository;
import edu.java.exception.exception.ListEmptyException;
import edu.java.service.process.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Primary
@RequiredArgsConstructor
public class JooqLinkService implements LinkService {
    private final JooqLinkRepository jooqLinkRepository;
    private final JooqLinkChatRepository jooqLinkChatRepository;

    @Override
    public LinkDTO add(LinkDTO linkDTO) {
        try {
            jooqLinkRepository.add(linkDTO);
            jooqLinkChatRepository.add(linkDTO);
            return linkDTO;
        } catch (Exception e) {

            throw new RuntimeException(e);
        }

    }

    @Override
    public Integer remove(LinkDTO linkDTO) {
        int rows = jooqLinkChatRepository.remove(linkDTO);
        return rows + jooqLinkRepository.remove(linkDTO);
    }

    @Override
    public List<LinkDTO> findAll(Long tgChatId) {
        List<LinkDTO> response = jooqLinkRepository.findAllByChatId(tgChatId);
        if(response.isEmpty()){
            throw new ListEmptyException("List empty for chat " + tgChatId);
        }
        return response;
    }
}
