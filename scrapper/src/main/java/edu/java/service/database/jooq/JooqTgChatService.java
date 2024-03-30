package edu.java.service.database.jooq;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.exception.exception.UserAlreadyExistException;
import edu.java.service.database.TgChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JooqTgChatService implements TgChatService {
    private final JooqChatRepository jooqChatRepository;

    @Override
    @Transactional
    public void add(Long tgChatId) {
        try {
            jooqChatRepository.add(tgChatId);
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional
    public void remove(Long tgChatId) {
        jooqChatRepository.remove(tgChatId);
    }

    @Override
    public List<ChatDTO> findAll() {
        return jooqChatRepository.findAll();
    }
}
