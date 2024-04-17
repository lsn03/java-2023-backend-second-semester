package edu.java.service.database.jpa;

import edu.java.domain.model.ChatDto;
import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.service.database.TgChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {
    private final JpaChatRepository jpaChatRepository;

    @Override
    @Transactional
    public void add(Long tgChatId) {
        jpaChatRepository.add(tgChatId);
    }

    @Override
    @Transactional
    public void remove(Long tgChatId) {
        jpaChatRepository.remove(tgChatId);
    }

    @Override
    public List<ChatDto> findAll() {
        return jpaChatRepository.findAll();
    }
}
