package edu.java.service.database.jpa;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.service.database.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
public class JpaTgChatService implements TgChatService {
    private final JpaChatRepository jpaChatRepository;
    @Override
    public void add(Long tgChatId) {
        jpaChatRepository.add(tgChatId);
    }

    @Override
    public void remove(Long tgChatId) {
        jpaChatRepository.remove(tgChatId);
    }

    @Override
    public List<ChatDTO> findAll() {
        return jpaChatRepository.findAll();
    }
}
