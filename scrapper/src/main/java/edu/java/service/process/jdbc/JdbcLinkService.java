package edu.java.service.process.jdbc;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.service.process.LinkService;
import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository jdbcLinkRepository;

    @Override
    public LinkDTO add(LinkDTO linkDTO) {
        return jdbcLinkRepository.add(linkDTO);
    }

    @Override
    public Integer remove(LinkDTO linkDTO) {
        return jdbcLinkRepository.remove(linkDTO);
    }

    @Override
    public Collection<LinkDTO> findAll(Long tgChatId) {
        return jdbcLinkRepository.findAll(tgChatId);
    }
}
