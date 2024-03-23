package edu.java.domain.repository.jpa;

import edu.java.domain.model.LinkDTO;
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
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaLinkChatRepository implements LinkChatRepository {
    private final JpaLinkChatRepositoryInterface jpaLinkChatRepository;
    private final EntityManager entityManager;
    private final JpaLinkRepositoryInterface jpaLinkRepository;
    private final JpaChatRepositoryInterface jpaChatRepository;

    @Override
    public void add(LinkDTO linkDTO) {

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
    public Integer remove(LinkDTO linkDTO) {

        return entityManager.createQuery("""
                delete from LinkChatEntity lce
                where lce.link.linkId = :linkId and lce.chat.id = : chatId
                """)
            .setParameter("linkId", linkDTO.getLinkId())
            .setParameter("chatId", linkDTO.getTgChatId())
            .executeUpdate();
    }

    @Override
    public Integer remove(Long tgChatId) {
        return jpaLinkChatRepository.deleteAllByChat_ChatId(tgChatId);
    }

    @Override
    public List<LinkDTO> findAllByChatId(Long tgChatId) {
        return entityManager.createQuery("""
                select le
                from LinkEntity le
                inner join LinkChatEntity lc
                    on lc.link.linkId = le.linkId
                where lc.chat.id = :chatId
                """, LinkEntity.class).setParameter("chatId", tgChatId)
            .getResultList().stream().map(linkEntity -> {
                var linkDTO = MapperLinkDTOLinkEntity.entityToDto(linkEntity);
                linkDTO.setTgChatId(tgChatId);
                return linkDTO;
            }).toList();
    }

    @Override
    public List<LinkDTO> findAllByLinkId(Long linkId) {
        var list =  entityManager.createQuery("""
                select le
                from LinkEntity le inner join LinkChatEntity lc on
                    lc.link.id = le.id where lc.link.id = :linkId
                """, LinkEntity.class)
            .setParameter("linkId", linkId)
            .getResultList();
        return list.stream().map(linkEntity ->{
            return MapperLinkDTOLinkEntity.entityToDto(linkEntity);
        }).toList();

    }
}