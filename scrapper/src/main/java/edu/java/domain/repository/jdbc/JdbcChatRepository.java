package edu.java.domain.repository.jdbc;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.ChatRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcChatRepository implements ChatRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void add(Long tgChatId) {
        if (findUserById(tgChatId)) {
            jdbcTemplate.update(
                "update chat set active = true where chat_id = (?)",
                tgChatId
            );
        } else {
            jdbcTemplate.update(
                "insert into chat (chat_id) values (?)",
                tgChatId
            );
        }
    }

    @Override
    @Transactional
    public void remove(Long tgChatId) {
        jdbcTemplate.update(
            "update chat set active = false where chat_id = (?)",
            tgChatId
        );
    }

    @Override
    @Transactional
    public List<ChatDTO> findAll() {
        return jdbcTemplate.query("select * from chat", (rs, rowNum) -> {
            return new ChatDTO(rs.getLong("chat_id"), rs.getBoolean("active"));
        });

    }

    private boolean findUserById(Long tgChatId) {
        try {
            Boolean isActive = jdbcTemplate.queryForObject(
                "select active from chat where chat_id = ? and active = false ",
                new Object[] {tgChatId},
                Boolean.class
            );
            return isActive != null || isActive.booleanValue();
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
