package edu.java.service.database.jpa;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.jpa.JpaLinkChatRepository;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.exception.exception.IncorrectParametersException;
import edu.java.exception.exception.LinkNotFoundException;
import edu.java.exception.exception.ListEmptyException;
import edu.java.service.database.LinkService;
import edu.java.service.handler.Handler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository jpaLinkRepository;
    private final JpaLinkChatRepository jpaLinkChatRepository;
    private final List<Handler> handlers;

    @Override
    @Transactional
    public LinkDTO add(LinkDTO linkDTO) {
        for (var handler : handlers) {
            if (handler.canHandle(linkDTO.getUri())) {
                Long linkId = jpaLinkRepository.findLinkIdByUrl(linkDTO.getUri());
                if (linkId == null) {
                    jpaLinkRepository.add(linkDTO);
                    linkId = linkDTO.getLinkId();
                }
                linkDTO.setLinkId(linkId);

                jpaLinkChatRepository.add(linkDTO);

                return linkDTO;
            }
        }
        throw new IncorrectParametersException("Error while hanlding link = " + linkDTO);
    }

    @Override
    @Transactional
    public Integer remove(LinkDTO linkDTO) {
        Long linkId = jpaLinkRepository.findLinkIdByUrl(linkDTO.getUri());
        linkDTO.setLinkId(linkId);
        int rows = jpaLinkChatRepository.remove(linkDTO);
        rows += jpaLinkRepository.remove(linkDTO);
        if (rows == 0) {
            throw new LinkNotFoundException("Link " + linkDTO.getUri() + " not found");
        }
        return rows;
    }

    @Override
    public List<LinkDTO> findAll(Long tgChatId) {
        List<LinkDTO> response = jpaLinkRepository.findAllByChatId(tgChatId);
        if (response.isEmpty()) {
            throw new ListEmptyException("List empty for chat " + tgChatId);
        }
        return response;
    }
}
