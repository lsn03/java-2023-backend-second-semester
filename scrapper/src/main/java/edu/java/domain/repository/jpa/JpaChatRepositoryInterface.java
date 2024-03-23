package edu.java.domain.repository.jpa;

import edu.java.domain.repository.jpa.entity.ChatEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepositoryInterface extends JpaRepository<ChatEntity, Long> {
    Optional<ChatEntity> findChatEntityByChatId(Long chatId);

}
