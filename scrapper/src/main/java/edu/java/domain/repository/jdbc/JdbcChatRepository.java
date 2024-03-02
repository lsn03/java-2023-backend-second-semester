package edu.java.domain.repository.jdbc;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.ChatRepository;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository

public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void add(ChatDTO chatDTO) {
        int a = jdbcTemplate.update("insert into chat (chat_id, active) values (?,?)",
            chatDTO.getChatId(),
            chatDTO.isActive());
        System.out.println(a);
    }

    @Override
    @Transactional
    public void remove(ChatDTO chatDTO) {

    }

    @Override
    @Transactional
    public void remove(Long id) {

    }

    @Override
    @Transactional
    public List<ChatDTO> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat", (rs, rowNum) -> {
            return new ChatDTO(rs.getLong("chat_id"), rs.getBoolean("active"));
        });

    }
}
