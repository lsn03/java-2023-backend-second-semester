package edu.java.service.process.jdbc;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.LinkChatRepository;
import edu.java.domain.repository.LinkRepository;
import edu.java.exception.exception.ListEmptyException;
import edu.java.exception.exception.RepeatTrackException;
import edu.java.exception.exception.UserDoesntExistException;
import edu.java.service.process.LinkService;
import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JdbcLinkService implements LinkService {
    private final LinkRepository jdbcLinkRepository;
    private final LinkChatRepository jdbcLinkChatRepository;

    @Override
    public LinkDTO add(LinkDTO linkDTO) {
        try {

            jdbcLinkRepository.add(linkDTO);
            jdbcLinkChatRepository.add(linkDTO);

            return linkDTO;
        } catch (DuplicateKeyException e) {
            Long id = jdbcLinkRepository.findUrl(linkDTO.getUri());
            linkDTO.setLinkId(id);
            jdbcLinkChatRepository.add(linkDTO);
            return linkDTO;
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("is not present in")) {
                throw new UserDoesntExistException(e);
            } else if (e.getMessage().contains("already exists")) {
                throw new RepeatTrackException(e);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Integer remove(LinkDTO linkDTO) {
        int affectRows = jdbcLinkChatRepository.remove(linkDTO);
        return affectRows + jdbcLinkRepository.remove(linkDTO);
    }

    @Override
    public Collection<LinkDTO> findAll(Long tgChatId) {
        var response = jdbcLinkRepository.findAllByChatId(tgChatId);
        if (response.isEmpty()) {
            throw new ListEmptyException("List empty for chat " + tgChatId);
        }
        return response;
    }
}
