package edu.java.domain.repository.jdbc;

import edu.java.domain.model.LinkDto;
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

@Repository
@AllArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private static final String LINK_ID = "link_id";
    private static final String URI = "uri";
    private static final String HASH = "hash";
    private static final String ADD_LINK = "insert into link (uri,created_at)  values (?,now())";
    private static final String FIND_LINK_ID_BY_URL = "select link_id from link where uri = ? limit 1 ";
    private static final String DELETE_BY_LINK_ID = "delete from link where link_id = (?) ;";
    private static final String FIND_ALL = "select link_id, uri, hash, created_at, last_update from link";
    private static final String UPDATE_LINK =
        "update link set uri = ?, last_update = now(), hash = ? where link_id = ? ";
    private static final String FIND_ALL_OLD_LINKS = """
        select lc.link_id,uri,created_at,last_update,hash,chat_id
             from link left join link_chat lc on
                link.link_id = lc.link_id
         where last_update is null or  last_update < now() - interval
        """;

    private final JdbcTemplate jdbcTemplate;
    private final JdbcLinkChatRepository jdbcLinkChatRepository;

    @Override
    public LinkDto add(LinkDto linkDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
            connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    ADD_LINK,
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
    public Long findUrl(URI uri) {

        try {
            Long id;
            id = jdbcTemplate.queryForObject(
                FIND_LINK_ID_BY_URL,
                new Object[] {uri.toString()},
                Long.class
            );
            return id;
        } catch (EmptyResultDataAccessException e) {
            throw new LinkNotFoundException("Link " + uri + " not found.");
        }

    }

    @Override
    public Integer remove(LinkDto linkDTO) {
        Long linkId = findUrl(linkDTO.getUri());
        linkDTO.setLinkId(linkId);
        int response = 0;
        List<LinkDto> list = jdbcLinkChatRepository.findAllByLinkId(linkDTO.getLinkId());
        if (list.isEmpty()) {
            response = jdbcTemplate.update(
                DELETE_BY_LINK_ID,
                linkDTO.getLinkId()
            );

        }
        return response;
    }

    @Override
    public List<LinkDto> findAllByChatId(Long tgChatId) {
        return jdbcLinkChatRepository.findAllByChatId(tgChatId);
    }

    @Override
    public List<LinkDto> findAll() {
        return jdbcTemplate.query(
            FIND_ALL,
            (rs, rowNum) -> new LinkDto(
                java.net.URI.create(rs.getString(URI)),
                null,
                rs.getLong(LINK_ID),
                rs.getString(HASH),
                null,
                null
            )
        );
    }

    @Override
    public void updateLink(LinkDto linkDTO) {
        jdbcTemplate.update(
            UPDATE_LINK,
            new Object[] {
                linkDTO.getUri().toString(),
                linkDTO.getHash(),
                linkDTO.getLinkId(),
            }
        );
    }

    @Override
    public List<LinkDto> findAllOldLinks(Integer timeInSeconds) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String interval = "'" + timeInSeconds + " seconds '";

        return jdbcTemplate.query(
            FIND_ALL_OLD_LINKS + interval,
            (rs, rowNum) -> {
                LinkDto linkDTO = new LinkDto();
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

                linkDTO.setHash(rs.getString(HASH));

                return linkDTO;
            }
        );
    }
}
