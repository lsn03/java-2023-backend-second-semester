package edu.java.domain.repository.jdbc;

import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.LinkChatRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcLinkChatRepository implements LinkChatRepository {
    private static final String URI = "uri";
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void add(LinkDto linkDTO) {
        jdbcTemplate.update(
            "insert into link_chat (link_id, chat_id) values (?, ?)",
            linkDTO.getLinkId(),
            linkDTO.getTgChatId()
        );
    }

    @Override
    @Transactional
    public Integer remove(LinkDto linkDTO) {
        return jdbcTemplate.update(
            "delete from link_chat where link_id = (?) and chat_id = (?);",
            linkDTO.getLinkId(),
            linkDTO.getTgChatId()
        );
    }

    @Override
    @Transactional
    public Integer remove(Long tgChatId) {
        return jdbcTemplate.update(
            "delete from link_chat where chat_id = (?);",
            tgChatId
        );
    }

    @Override
    @Transactional
    public List<LinkDto> findAllByChatId(Long tgChatId) {
        return jdbcTemplate.query(
            "select link.link_id,uri "
                + "from link inner join link_chat c on c.link_id = link.link_id where c.chat_id = (?)",
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
    @Transactional
    public List<LinkDto> findAllByLinkId(Long linkId) {
        return jdbcTemplate.query(
            "select chat_id, uri, c.link_id "
                + "from link inner join link_chat c on c.link_id = link.link_id where c.link_id = (?)",
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
