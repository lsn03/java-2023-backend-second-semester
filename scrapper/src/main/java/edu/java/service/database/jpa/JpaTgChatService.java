package edu.java.service.database.jpa;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.service.database.TgChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Primary
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
    @Transactional
    public List<ChatDTO> findAll() {
        return jpaChatRepository.findAll();
    }
}