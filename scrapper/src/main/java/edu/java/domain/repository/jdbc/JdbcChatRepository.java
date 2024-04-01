package edu.java.domain.repository.jdbc;

import edu.java.domain.model.ChatDto;
import edu.java.domain.repository.ChatRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class JdbcChatRepository implements ChatRepository {

    private static final String UPDATE_CHAT_SET_ACTIVE_TRUE_WHERE_CHAT_ID =
        "update chat set active = true where chat_id = (?)";
    private static final String INSERT_INTO_CHAT_CHAT_ID_VALUES = "insert into chat (chat_id) values (?)";
    private static final String REMOVE_BY_CHAT_ID =
        "update chat set active = false where chat_id = (?)";
    private static final String SELECT_FROM_CHAT = "select * from chat";
    private static final String FIND_INACTIVE_USER = "select active from chat where chat_id = ? and active = false";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(Long tgChatId) {
        if (findInActiveUserById(tgChatId)) {
            jdbcTemplate.update(
                UPDATE_CHAT_SET_ACTIVE_TRUE_WHERE_CHAT_ID,
                tgChatId
            );
        } else {
            jdbcTemplate.update(
                INSERT_INTO_CHAT_CHAT_ID_VALUES,
                tgChatId
            );
        }
    }

    @Override
    public void remove(Long tgChatId) {
        jdbcTemplate.update(
            REMOVE_BY_CHAT_ID,
            tgChatId
        );
    }

    @Override
    @Transactional
    public List<ChatDto> findAll() {
        return jdbcTemplate.query("select * from chat", (rs, rowNum) -> {
            return new ChatDto(rs.getLong("chat_id"), rs.getBoolean("active"));
        });

    }

    protected boolean findInActiveUserById(Long tgChatId) {
        try {
            Boolean notActive = jdbcTemplate.queryForObject(
                FIND_INACTIVE_USER,
                new Object[] {tgChatId},
                Boolean.class
            );

            return notActive != null && !notActive;

        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
