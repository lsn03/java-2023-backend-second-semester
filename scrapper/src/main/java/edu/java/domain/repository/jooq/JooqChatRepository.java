package edu.java.domain.repository.jooq;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.ChatRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class JooqChatRepository implements ChatRepository {
    @Override
    public void add(Long tgChatId) {

    }

    @Override
    public void remove(Long tgChatId) {

    }

    @Override
    public List<ChatDTO> findAll() {
        return null;
    }
}
