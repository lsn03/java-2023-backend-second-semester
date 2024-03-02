package edu.java.domain.repository;

import edu.java.domain.model.ChatDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface ChatRepository {

    void add(ChatDTO chatDTO);
    void remove(ChatDTO chatDTO);
    void remove(Long id);
    List<ChatDTO> findAll();
}
