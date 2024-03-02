package edu.java.domain.repository.jdbc;

import edu.java.domain.model.LinkChatDTO;
import edu.java.domain.repository.LinkChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkChatRepository implements LinkChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void add(LinkChatDTO linkDTO) {

    }

    @Override
    @Transactional
    public void remove(LinkChatDTO linkDTO) {

    }

    @Override
    @Transactional
    public void remove(Long chatId, Long linkId) {

    }

    @Override
    @Transactional
    public List<LinkChatDTO> findAll() {
        return null;
    }
}
