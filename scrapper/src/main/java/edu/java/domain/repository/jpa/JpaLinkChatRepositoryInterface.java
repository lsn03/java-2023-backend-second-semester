package edu.java.domain.repository.jpa;

import edu.java.domain.repository.jpa.entity.LinkChatEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@SuppressWarnings("MethodName")
public interface JpaLinkChatRepositoryInterface extends JpaRepository<LinkChatEntity, Long> {
    Integer deleteAllByChat_ChatId(Long chatId);

    Optional<LinkChatEntity> findByChat_ChatIdAndLink_LinkId(Long chatId, Long linkId);
}
