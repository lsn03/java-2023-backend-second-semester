package edu.java.service.database.jooq;

import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.jooq.JooqLinkChatRepository;
import edu.java.domain.repository.jooq.JooqLinkRepository;
import edu.java.exception.exception.IncorrectParametersException;
import edu.java.exception.exception.ListEmptyException;
import edu.java.exception.exception.RepeatTrackException;
import edu.java.exception.exception.UserDoesntExistException;
import edu.java.service.database.LinkService;
import edu.java.service.handler.Handler;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JooqLinkService implements LinkService {
    private final JooqLinkRepository jooqLinkRepository;
    private final JooqLinkChatRepository jooqLinkChatRepository;

    private final List<Handler> handlers;

    @Override
    @Transactional
    public LinkDto add(LinkDto linkDTO) {

        for (var handler : handlers) {
            if (handler.canHandle(linkDTO.getUri())) {

                Long linkId = jooqLinkRepository.findLinkIdByUrl(linkDTO.getUri());
                if (linkId == null) {
                    if (linkDTO.getCreatedAt() == null) {
                        linkDTO.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));
                    }

                    jooqLinkRepository.add(linkDTO);
                    linkId = linkDTO.getLinkId();

                }
                linkDTO.setLinkId(linkId);
                try {
                    jooqLinkChatRepository.add(linkDTO);
                } catch (DuplicateKeyException e) {
                    if (e.getMessage().contains("already exists")) {
                        throw new RepeatTrackException(e);
                    }
                    throw new RuntimeException(e);
                } catch (DataIntegrityViolationException e) {
                    if (e.getMessage().contains("is not present in")) {
                        throw new UserDoesntExistException(e);
                    }
                    throw new RuntimeException(e);
                }
                return linkDTO;
            }
        }
        throw new IncorrectParametersException("Error while hanlding link = " + linkDTO);

    }

    @Override
    @Transactional
    public Integer remove(LinkDto linkDTO) {
        Long linkId = jooqLinkRepository.findLinkIdByUrl(linkDTO.getUri());
        linkDTO.setLinkId(linkId);
        int rows = jooqLinkChatRepository.remove(linkDTO);
        return rows + jooqLinkRepository.remove(linkDTO);
    }

    @Override
    public List<LinkDto> findAll(Long tgChatId) {
        List<LinkDto> response = jooqLinkRepository.findAllByChatId(tgChatId);
        if (response.isEmpty()) {
            throw new ListEmptyException("List empty for chat " + tgChatId);
        }
        return response;
    }
}
