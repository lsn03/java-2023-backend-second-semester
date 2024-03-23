package edu.java.domain.repository.jdbc;

import edu.java.domain.model.LinkDTO;
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
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private static final String LINK_ID = "link_id";
    public static final String URI_FIELD = "uri";
    public static final String HASH = "hash";
    private final JdbcTemplate jdbcTemplate;
    private final JdbcLinkChatRepository jdbcLinkChatRepository;

    @Override
    @Transactional
    public LinkDTO add(LinkDTO linkDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        var create = linkDTO.getCreatedAt();
        jdbcTemplate.update(
            connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "insert into link (uri,created_at)  values (?,?)",
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
                "select link_id from link where uri = ? limit 1 ",
                new Object[] {uri.toString()},
                Long.class
            );
            return id;
        } catch (EmptyResultDataAccessException e) {
            return null;
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
    public List<LinkDTO> findAllByLinkId(Long linkId) {
        return jdbcLinkChatRepository.findAllByLinkId(linkId);
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
            "select link_id, uri, created_at, last_update from link",
            (rs, rowNum) -> new LinkDTO(
                java.net.URI.create(rs.getString(URI_FIELD)),
                null,
                rs.getLong(LINK_ID),

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

        String interval = "'" + timeInSeconds + " seconds '";

        return jdbcTemplate.query(
            "select link_id,uri,created_at,last_update "
                + "from link "
                + "where last_update is null or  last_update < now() - interval " + interval,
            (rs, rowNum) -> {
                LinkDTO linkDTO = new LinkDTO();
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
