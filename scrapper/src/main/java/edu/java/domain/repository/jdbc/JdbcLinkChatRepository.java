package edu.java.domain.repository.jdbc;

import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.LinkChatRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@AllArgsConstructor
public class JdbcLinkChatRepository implements LinkChatRepository {
    private static final String URI = "uri";
    private static final String ADD_LINK =
        "insert into link_chat (link_id, chat_id) values (?, ?)";
    private static final String REMOVE_BY_LINK_ID_AND_CHAT_ID =
        "delete from link_chat where link_id = (?) and chat_id = (?);";
    private static final String REMOVE_BY_CHAT_ID = "delete from link_chat where chat_id = (?);";
    private static final String FIND_ALL_BNY_CHAT_ID = """
        select link.link_id,uri
           from link inner join link_chat c on
           c.link_id = link.link_id where c.chat_id = (?)
        """;
    private static final String FIND_ALL_BY_LINK_ID = """
        select chat_id, uri, c.link_id
            from link inner join link_chat c on
        c.link_id = link.link_id where c.link_id = (?)""";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(LinkDto linkDTO) {
        jdbcTemplate.update(
            ADD_LINK,
            linkDTO.getLinkId(),
            linkDTO.getTgChatId()
        );
    }

    @Override
    public Integer remove(LinkDto linkDTO) {
        return jdbcTemplate.update(
            REMOVE_BY_LINK_ID_AND_CHAT_ID,
            linkDTO.getLinkId(),
            linkDTO.getTgChatId()
        );
    }

    @Override
    public Integer remove(Long tgChatId) {
        return jdbcTemplate.update(
            REMOVE_BY_CHAT_ID,
            tgChatId
        );
    }

    @Override
    public List<LinkDto> findAllByChatId(Long tgChatId) {
        return jdbcTemplate.query(
            FIND_ALL_BNY_CHAT_ID,
            new Object[] {tgChatId},
            (rs, rowNum) -> {
                var ans = new LinkDto();
                ans.setTgChatId(tgChatId);
                ans.setLinkId(rs.getLong("link_id"));
                ans.setUri(java.net.URI.create(rs.getString(URI)));
                return ans;
            }
        );
    }

    @Override
    public List<LinkDto> findAllByLinkId(Long linkId) {
        return jdbcTemplate.query(
            FIND_ALL_BY_LINK_ID,
            new Object[] {linkId},
            (rs, rowNum) -> {
                var ans = new LinkDto();
                ans.setLinkId(linkId);
                ans.setTgChatId(rs.getLong("chat_id"));
                ans.setUri(java.net.URI.create(rs.getString(URI)));
                return ans;
            }
        );
    }
}
