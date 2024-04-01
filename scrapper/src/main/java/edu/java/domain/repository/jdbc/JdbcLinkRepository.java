package edu.java.domain.repository.jdbc;

import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.LinkRepository;
import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    private static final String URI_FIELD = "uri";

    private static final String ADD_LINK = "insert into link (uri,created_at)  values (?, ?)";
    private static final String FIND_LINK_ID_BY_URL = "select link_id from link where uri = ? limit 1 ";
    private static final String DELETE_BY_LINK_ID = "delete from link where link_id = (?) ;";
    private static final String FIND_ALL = "select link_id, uri, created_at, last_update from link";
    private static final String UPDATE_LINK =
        "update link set uri = ?, last_update = now() where link_id = ? ";
    private static final String FIND_ALL_OLD_LINKS = """
        select lc.link_id,uri,created_at,last_update ,chat_id
             from link left join link_chat lc on
                link.link_id = lc.link_id
         where last_update is null or  last_update < now() - interval
        """;

    private final JdbcTemplate jdbcTemplate;
    private final JdbcLinkChatRepository jdbcLinkChatRepository;

    @Override
    public LinkDto add(LinkDto linkDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        var create = linkDTO.getCreatedAt();
        jdbcTemplate.update(
            connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    ADD_LINK,
                    new String[] {LINK_ID}
                );
                ps.setString(1, String.valueOf(linkDTO.getUri()));
                ps.setObject(
                    2,
                    create == null ? LocalDateTime.now() : create.toLocalDateTime()
                );
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
                FIND_LINK_ID_BY_URL,
                new Object[] {uri.toString()},
                Long.class
            );
            return id;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public Integer remove(LinkDto linkDTO) {
        Long linkId = findLinkIdByUrl(linkDTO.getUri());
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
    public List<LinkDto> findAllByLinkId(Long linkId) {
        return jdbcLinkChatRepository.findAllByLinkId(linkId);
    }

    @Override
    public List<LinkDto> findAll() {
        return jdbcTemplate.query(
            FIND_ALL,
            (rs, rowNum) -> new LinkDto(
                java.net.URI.create(rs.getString(URI_FIELD)),
                null,
                rs.getLong(LINK_ID),

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

                linkDTO.getLinkId(),
            }
        );
    }

    @Override
    public List<LinkDto> findAllOldLinks(Integer timeInSeconds) {

        String interval = "'" + timeInSeconds + " seconds '";

        return jdbcTemplate.query(
            FIND_ALL_OLD_LINKS + interval,
            (rs, rowNum) -> {
                LinkDto linkDTO = new LinkDto();
                linkDTO.setLinkId(rs.getLong(LINK_ID));
                linkDTO.setUri(URI.create(rs.getString(URI_FIELD)));
                linkDTO.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime().atOffset(ZoneOffset.UTC));

                Timestamp lastUpdate = rs.getTimestamp("last_update");
                if (lastUpdate != null) {
                    linkDTO.setLastUpdate(lastUpdate.toLocalDateTime().atOffset(ZoneOffset.UTC));
                }

                return linkDTO;
            }
        );
    }
}
