package edu.java.service.database.jdbc;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.exception.exception.UserAlreadyExistException;
import edu.java.service.database.TgChatService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final JdbcChatRepository jdbcChatRepository;

    @Override
    public void add(Long tgChatId) {
        try {
            jdbcChatRepository.add(tgChatId);
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistException(e.getMessage(), e.getCause());
        }

    }

    @Override
    public void remove(Long tgChatId) {
        jdbcChatRepository.remove(tgChatId);
    }

    @Override
    public List<ChatDTO> findAll() {
        return jdbcChatRepository.findAll();
    }
}
