package edu.java.domain.repository.jdbc;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.LinkRepository;
import edu.java.exception.exception.LinkNotFoundException;
import java.net.URI;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final JdbcLinkChatRepository jdbcLinkChatRepository;

    @Override
    @Transactional
    public LinkDTO add(LinkDTO linkDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
            connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "insert into link (uri) values (?)",
                    new String[] {"link_id"} // Имя столбца сгенерированного ключа
                );
                ps.setString(1, String.valueOf(linkDTO.getUri()));
                return ps;
            },
            keyHolder
        );
        Number key = keyHolder.getKey();
        Objects.requireNonNull(key);

        linkDTO.setLinkId(key.longValue());

        jdbcLinkChatRepository.add(linkDTO);

        return linkDTO;
    }

    @Override
    public Long findUrl(URI uri) {

        try {
            Long id;
            id = jdbcTemplate.queryForObject(
                "select link_id from link where uri = ? limit 1 ",
                new Object[] {uri.toString()},
                Long.class
            );
            return id;
        } catch (EmptyResultDataAccessException e) {
            throw new LinkNotFoundException("Link " + uri + " not found.");
        }

    }

    @Override
    @Transactional
    public Integer remove(LinkDTO linkDTO) {
        Long linkId = findUrl(linkDTO.getUri());
        linkDTO.setLinkId(linkId);
        int response = jdbcLinkChatRepository.remove(linkDTO);
        List<LinkDTO> list = jdbcLinkChatRepository.findAllByLinkId(linkDTO.getLinkId());
        if (list.isEmpty()) {
            response += jdbcTemplate.update(
                "delete from link where link_id = (?) ;",
                linkDTO.getLinkId()
            );

        }
        return response;
    }

    @Override
    @Transactional
    public List<LinkDTO> findAll(Long tgChatId) {
        return jdbcLinkChatRepository.findAllByChatId(tgChatId);
    }

}
