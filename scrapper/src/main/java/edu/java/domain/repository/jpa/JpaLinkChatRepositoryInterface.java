package edu.java.domain.repository.jpa;

import edu.java.domain.repository.jpa.entity.LinkChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaLinkChatRepositoryInterface extends JpaRepository<LinkChatEntity, Long> {
    Integer deleteAllByChat_ChatId(Long chatId);
    Optional<LinkChatEntity>  findByChat_ChatIdAndLink_LinkId(Long chatId, Long linkId);
}
