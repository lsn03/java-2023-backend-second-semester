package edu.java.service.process.jdbc;

import edu.java.domain.model.ChatDto;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.exception.exception.UserAlreadyExistException;
import edu.java.service.process.TgChatService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final JdbcChatRepository jdbcChatRepository;

    @Override
    @Transactional
    public void add(Long tgChatId) {
        try {
            jdbcChatRepository.add(tgChatId);
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistException(e.getMessage(), e.getCause());
        }

    }

    @Override
    @Transactional
    public void remove(Long tgChatId) {
        jdbcChatRepository.remove(tgChatId);
    }

    @Override
    public List<ChatDto> findAll() {
        return jdbcChatRepository.findAll();
    }
}
