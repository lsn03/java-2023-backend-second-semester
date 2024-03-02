package edu.java.domain.repository.jdbc;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.LinkChatRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcLinkChatRepository implements LinkChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void add(LinkDTO linkDTO) {
        jdbcTemplate.update(
            "insert into link_chat (link_id, chat_id) values (?, ?)",
            linkDTO.getLinkId(),
            linkDTO.getTgChatId()
        );
    }

    @Override
    @Transactional
    public Integer remove(LinkDTO linkDTO) {
        return jdbcTemplate.update(
            "delete from link_chat where link_id = (?) and chat_id = (?);",
            linkDTO.getLinkId(),
            linkDTO.getTgChatId()
        );
    }

    @Override
    @Transactional
    public List<LinkDTO> findAll(Long tgChatId) {
        return jdbcTemplate.query(
            "select * from link_chat where chat_id = (?)",
            new Object[] {tgChatId},
            (rs, rowNum) -> {
                var ans = new LinkDTO();
                ans.setTgChatId(rs.getLong("chat_id"));
                ans.setLinkId(rs.getLong("link_id"));
                return ans;
            }
        );
    }
}
