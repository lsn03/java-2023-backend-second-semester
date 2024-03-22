package edu.java.domain.repository.jpa;

import edu.java.domain.repository.jpa.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaChatRepositoryInterface extends JpaRepository<ChatEntity, Long> {
    Optional<ChatEntity> findChatEntityByChatId(Long chatId);

}
