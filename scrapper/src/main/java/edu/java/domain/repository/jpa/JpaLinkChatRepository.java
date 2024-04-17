package edu.java.domain.repository.jpa;

import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.LinkChatRepository;
import edu.java.domain.repository.jpa.entity.ChatEntity;
import edu.java.domain.repository.jpa.entity.LinkChatEntity;
import edu.java.domain.repository.jpa.entity.LinkEntity;
import edu.java.domain.repository.jpa.mapper.MapperLinkDTOLinkEntity;
import edu.java.exception.exception.RepeatTrackException;
import edu.java.exception.exception.UserDoesntExistException;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkChatRepository implements LinkChatRepository {
    public static final String CHAT_ID = "chatId";
    public static final String LINK_ID = "linkId";
    private final JpaLinkChatRepositoryInterface jpaLinkChatRepository;
    private final EntityManager entityManager;
    private final JpaLinkRepositoryInterface jpaLinkRepository;
    private final JpaChatRepositoryInterface jpaChatRepository;

    @Override
    public void add(LinkDto linkDTO) {

        var entity = jpaLinkChatRepository.findByChat_ChatIdAndLink_LinkId(linkDTO.getTgChatId(), linkDTO.getLinkId());
        if (entity.isPresent()) {
            throw new RepeatTrackException();
        }
        Optional<LinkEntity> linkEntity = jpaLinkRepository.findById(linkDTO.getLinkId());
        Optional<ChatEntity> chatEntity = jpaChatRepository.findById(linkDTO.getTgChatId());
        if (linkEntity.isPresent() && chatEntity.isPresent()) {

            LinkChatEntity linkChatEntity = new LinkChatEntity();
            linkChatEntity.setChat(chatEntity.get());
            linkChatEntity.setLink(linkEntity.get());
            jpaLinkChatRepository.save(linkChatEntity);
        } else {
            throw new UserDoesntExistException("User with id " + linkDTO.getTgChatId() + " is not exist");
        }
    }

    @Override
    public Integer remove(LinkDto linkDTO) {

        return entityManager.createQuery("""
                delete from LinkChatEntity lce
                where lce.link.linkId = :linkId and lce.chat.id = : chatId
                """)
            .setParameter(LINK_ID, linkDTO.getLinkId())
            .setParameter(CHAT_ID, linkDTO.getTgChatId())
            .executeUpdate();
    }

    @Override
    public Integer remove(Long tgChatId) {
        return jpaLinkChatRepository.deleteAllByChat_ChatId(tgChatId);
    }

    @Override
    public List<LinkDto> findAllByChatId(Long tgChatId) {
        return entityManager.createQuery("""
                select le
                from LinkEntity le
                inner join LinkChatEntity lc
                    on lc.link.linkId = le.linkId
                where lc.chat.id = :chatId
                """, LinkEntity.class).setParameter(CHAT_ID, tgChatId)
            .getResultList().stream().map(linkEntity -> {
                var linkDTO = MapperLinkDTOLinkEntity.entityToDto(linkEntity);
                linkDTO.setTgChatId(tgChatId);
                return linkDTO;
            }).toList();
    }

    @Override
    public List<LinkDto> findAllByLinkId(Long linkId) {
        var list = entityManager.createQuery("""
                select le
                from LinkEntity le inner join LinkChatEntity lc on
                    lc.link.id = le.id where lc.link.id = :linkId
                """, LinkEntity.class)
            .setParameter(LINK_ID, linkId)
            .getResultList();
        return list.stream().map(MapperLinkDTOLinkEntity::entityToDto).toList();

    }
}
