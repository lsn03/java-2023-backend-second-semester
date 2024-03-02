package edu.java.service.process.jdbc;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.service.process.TgChatService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final JdbcChatRepository chatRepository;

    @Override
    public void add(Long tgChatId) {

        chatRepository.add(tgChatId);

    }

    @Override
    public void remove(Long tgChatId) {
        chatRepository.remove(tgChatId);
    }

    @Override
    public List<ChatDTO> findAll() {
        return chatRepository.findAll();
    }
}
