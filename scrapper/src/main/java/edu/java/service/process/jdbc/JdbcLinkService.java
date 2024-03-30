package edu.java.service.process.jdbc;

import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.jdbc.JdbcLinkChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.exception.exception.ListEmptyException;
import edu.java.exception.exception.RepeatTrackException;
import edu.java.exception.exception.UserDoesntExistException;
import edu.java.service.process.LinkService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcLinkChatRepository jdbcLinkChatRepository;

    @Override
    @Transactional
    public LinkDto add(LinkDto linkDTO) {
        try {

            jdbcLinkRepository.add(linkDTO);
            jdbcLinkChatRepository.add(linkDTO);

            return linkDTO;
        } catch (DuplicateKeyException e) {
            Long id = jdbcLinkRepository.findUrl(linkDTO.getUri());
            linkDTO.setLinkId(id);
            try {
                jdbcLinkChatRepository.add(linkDTO);
            } catch (DuplicateKeyException duplicateKeyException) {
                if (e.getMessage().contains("already exists")) {
                    throw new RepeatTrackException(e);
                } else {
                    throw new RuntimeException(e);
                }
            }
            return linkDTO;
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("is not present in")) {
                throw new UserDoesntExistException(e);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @Transactional
    public Integer remove(LinkDto linkDTO) {
        int affectRows = jdbcLinkChatRepository.remove(linkDTO);
        return affectRows + jdbcLinkRepository.remove(linkDTO);
    }

    @Override
    public List<LinkDto> findAll(Long tgChatId) {
        var response = jdbcLinkRepository.findAllByChatId(tgChatId);
        if (response.isEmpty()) {
            throw new ListEmptyException("List empty for chat " + tgChatId);
        }
        return response;
    }
}
