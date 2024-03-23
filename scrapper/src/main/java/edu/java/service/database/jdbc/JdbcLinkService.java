package edu.java.service.database.jdbc;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.jdbc.JdbcLinkChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.exception.exception.IncorrectParametersException;
import edu.java.exception.exception.ListEmptyException;
import edu.java.exception.exception.RepeatTrackException;
import edu.java.exception.exception.UserDoesntExistException;
import edu.java.service.database.LinkService;
import edu.java.service.handler.Handler;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcLinkChatRepository jdbcLinkChatRepository;
    private final List<Handler> handlers;

    @Override
    @Transactional
    public LinkDTO add(LinkDTO linkDTO) {
        for (var handler : handlers) {
            if (handler.canHandle(linkDTO.getUri())) {

                Long linkId = jdbcLinkRepository.findLinkIdByUrl(linkDTO.getUri());
                if (linkId == null) {
                    jdbcLinkRepository.add(linkDTO);
                    linkId = linkDTO.getLinkId();
                }
                linkDTO.setLinkId(linkId);
                try {
                    jdbcLinkChatRepository.add(linkDTO);
                } catch (DuplicateKeyException e) {
                    if (e.getMessage().contains("already exists")) {
                        throw new RepeatTrackException(e);
                    }

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
    public Integer remove(LinkDTO linkDTO) {
        Long linkId = jdbcLinkRepository.findLinkIdByUrl(linkDTO.getUri());
        linkDTO.setLinkId(linkId);
        int affectRows = jdbcLinkChatRepository.remove(linkDTO);
        return affectRows + jdbcLinkRepository.remove(linkDTO);
    }

    @Override
    @Transactional
    public List<LinkDTO> findAll(Long tgChatId) {
        var response = jdbcLinkRepository.findAllByChatId(tgChatId);
        if (response.isEmpty()) {
            throw new ListEmptyException("List empty for chat " + tgChatId);
        }
        return response;
    }
}
