package edu.java.domain.repository.jdbc;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.LinkRepository;
import edu.java.exception.exception.LinkNotFoundException;
import java.net.URI;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
    private static final String LINK_ID = "link_id";
    public static final String URI = "uri";
    public static final String HASH = "hash";
    private final JdbcTemplate jdbcTemplate;
    private final JdbcLinkChatRepository jdbcLinkChatRepository;

    @Override
    @Transactional
    public LinkDTO add(LinkDTO linkDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
            connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "insert into link (uri,created_at)  values (?,now())",
                    new String[] {LINK_ID}
                );
                ps.setString(1, String.valueOf(linkDTO.getUri()));
                return ps;
            },
            keyHolder
        );
        Number key = keyHolder.getKey();
        Objects.requireNonNull(key);

        linkDTO.setLinkId(key.longValue());

        return linkDTO;
    }

    @Override

    public Long findLinkIdByUrl(URI uri) {

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
        Long linkId = findLinkIdByUrl(linkDTO.getUri());
        linkDTO.setLinkId(linkId);
        int response = 0;
        List<LinkDTO> list = jdbcLinkChatRepository.findAllByLinkId(linkDTO.getLinkId());
        if (list.isEmpty()) {
            response = jdbcTemplate.update(
                "delete from link where link_id = (?) ;",
                linkDTO.getLinkId()
            );

        }
        return response;
    }

    @Override
    @Transactional
    public List<LinkDTO> findAllByChatId(Long tgChatId) {
        return jdbcLinkChatRepository.findAllByChatId(tgChatId);
    }

    @Override
    @Transactional
    public List<LinkDTO> findAll() {
        return jdbcTemplate.query(
            "select link_id, uri, created_at, last_update,site_type_id from link",
            (rs, rowNum) -> new LinkDTO(
                java.net.URI.create(rs.getString(URI)),
                null,
                rs.getLong(LINK_ID),
                rs.getInt("site_type_id"),
                null,
                null
            )
        );
    }

    @Transactional
    @Override
    public void updateLink(LinkDTO linkDTO) {
        jdbcTemplate.update(
            "update link set uri = ?, last_update = now() where link_id = ? ",
            new Object[] {
                linkDTO.getUri().toString(),

                linkDTO.getLinkId(),
            }
        );
    }

    @Override
    @Transactional
    public List<LinkDTO> findAllOldLinks(Integer timeInSeconds) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String interval = "'" + timeInSeconds + " seconds '";

        return jdbcTemplate.query(
            "select lc.link_id,uri,created_at,last_update,hash,chat_id "
                + "from link left join link_chat lc on link.link_id = lc.link_id "
                + "where last_update is null or  last_update < now() - interval " + interval,
            (rs, rowNum) -> {
                LinkDTO linkDTO = new LinkDTO();
                String createdAt = rs.getString("created_at");
                int index = createdAt.lastIndexOf(".");
                createdAt = createdAt.substring(0, index);
                LocalDateTime localDateTimeCreatedAt = LocalDateTime.parse(createdAt, formatter);

                var lastUpdateString = rs.getString("last_update");
                LocalDateTime lastUpdate;
                if (lastUpdateString == null) {
                    lastUpdate = OffsetDateTime.now().toLocalDateTime();
                } else {
                    index = lastUpdateString.lastIndexOf(".");
                    lastUpdateString = lastUpdateString.substring(0, index);
                    lastUpdate = LocalDateTime.parse(lastUpdateString, formatter);
                }
                linkDTO.setLastUpdate(lastUpdate.atOffset(ZoneOffset.UTC));
                linkDTO.setTgChatId(rs.getLong("chat_id"));
                linkDTO.setLinkId(rs.getLong(LINK_ID));
                linkDTO.setUri(java.net.URI.create(rs.getString(URI)));
                linkDTO.setCreatedAt(localDateTimeCreatedAt.atOffset(ZoneOffset.UTC));



                return linkDTO;
            }
        );
    }
}
