package edu.java.domain.repository.jpa;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.ChatRepository;
import edu.java.domain.repository.jpa.entity.ChatEntity;
import edu.java.domain.repository.jpa.mapper.MapperChatDTOChatEntity;
import edu.java.exception.exception.UserAlreadyExistException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JpaChatRepository implements ChatRepository {
    private final JpaChatRepositoryInterface jpaChatRepository;

    @Override
    public void add(Long tgChatId) {
        Optional<ChatEntity> chatEntity = jpaChatRepository.findChatEntityByChatId(tgChatId);
        ChatEntity chat;
        if (chatEntity.isPresent()) {
            chat = chatEntity.get();
            if (chat.getChatId() != null && chat.getActive() != null && chat.getActive()) {
                throw new UserAlreadyExistException("User already exist");
            }
            chat.setActive(true);
        } else {
            chat = new ChatEntity();
            chat.setChatId(tgChatId);
            chat.setActive(true);
        }

        jpaChatRepository.save(chat);
    }

    @Override
    public void remove(Long tgChatId) {
        jpaChatRepository.findChatEntityByChatId(tgChatId).ifPresent(chat -> {
            chat.setActive(false);
            jpaChatRepository.save(chat);
        });
    }

    @Override
    public List<ChatDTO> findAll() {

        List<ChatEntity> entityList = jpaChatRepository.findAll();
        return entityList.stream().map(MapperChatDTOChatEntity::entityToDto).toList();

    }
}
