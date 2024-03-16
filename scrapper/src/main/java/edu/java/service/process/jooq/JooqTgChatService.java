package edu.java.service.process.jooq;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.exception.exception.UserAlreadyExistException;
import edu.java.service.process.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@Primary
public class JooqTgChatService implements TgChatService {
    private final JooqChatRepository jooqChatRepository;

    @Override
    public void add(Long tgChatId) {
        try {
            jooqChatRepository.add(tgChatId);
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void remove(Long tgChatId) {
        jooqChatRepository.remove(tgChatId);
    }

    @Override
    public List<ChatDTO> findAll() {
        return jooqChatRepository.findAll();
    }
}
