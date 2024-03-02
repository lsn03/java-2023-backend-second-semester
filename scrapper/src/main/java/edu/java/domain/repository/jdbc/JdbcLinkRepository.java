package edu.java.domain.repository.jdbc;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.LinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void add(LinkDTO linkDTO) {

    }

    @Override
    @Transactional
    public void remove(LinkDTO linkDTO) {

    }

    @Override
    @Transactional
    public void remove(Long id) {

    }

    @Override
    @Transactional
    public void remove(String url) {

    }

    @Override
    @Transactional
    public List<LinkDTO> findAll() {
        return null;
    }
}
