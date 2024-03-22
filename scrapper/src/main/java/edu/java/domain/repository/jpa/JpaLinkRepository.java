package edu.java.domain.repository.jpa;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.LinkRepository;
import edu.java.domain.repository.jpa.entity.LinkEntity;
import edu.java.domain.repository.jpa.mapper.MapperLinkDTOLinkEntity;
import edu.java.exception.exception.RepeatTrackException;
import jakarta.persistence.EntityManager;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JpaLinkRepository implements LinkRepository {
    private final JpaLinkRepositoryInterface jpaLinkRepository;
    private final EntityManager entityManager;
    private final JpaLinkChatRepository jpaLinkChatRepository;

    @Override
    public LinkDTO add(LinkDTO linkDTO) {
        var entity =
            jpaLinkRepository.findLinkEntityByUri(linkDTO.getUri().toString());
        if (entity.isPresent()) {
            throw new RepeatTrackException();
        }
        var inserted = MapperLinkDTOLinkEntity.dtoToEntity(linkDTO);
        jpaLinkRepository.save(inserted);

        linkDTO.setLinkId(inserted.getLinkId());
        return linkDTO;
    }

    @Override
    public Integer remove(LinkDTO linkDTO) {
        var countLinkChat = entityManager.createQuery(
                "select count(lc) from LinkChatEntity lc where lc.link.id = :linkId", Long.class)
            .setParameter("linkId", linkDTO.getLinkId())
            .getSingleResult();
        if (countLinkChat > 0) {
            return 0;
        }

        return entityManager.createQuery("delete from LinkEntity le where le.uri = :uri")
            .setParameter("uri", linkDTO.getUri().toString())
            .executeUpdate();
    }

    @Override
    public List<LinkDTO> findAllByChatId(Long tgChatId) {
        return jpaLinkChatRepository.findAllByChatId(tgChatId);
    }

    @Override
    public List<LinkDTO> findAllByLinkId(Long linkId) {
        return jpaLinkChatRepository.findAllByLinkId(linkId);
    }

    @Override
    public Long findLinkIdByUrl(URI uri) {
        var entity = jpaLinkRepository.findLinkEntityByUri(uri.toString());
        return entity.map(LinkEntity::getLinkId).orElse(null);
    }

    @Override
    public List<LinkDTO> findAll() {
        var entityList = jpaLinkRepository.findAll();
        return entityList.stream().map(MapperLinkDTOLinkEntity::entityToDto).toList();
    }

    @Override
    public void updateLink(LinkDTO elem) {
        var entity = MapperLinkDTOLinkEntity.dtoToEntity(elem);
        entity.setLastUpdate(LocalDateTime.now());
        jpaLinkRepository.save(entity);
    }

    @Override
    public List<LinkDTO> findAllOldLinks(Integer timeInSeconds) {
        LocalDateTime differenceTime = LocalDateTime.now().minusSeconds(timeInSeconds);
        var entityList = entityManager.createQuery("""
                select le.linkId,le.uri,le.createdAt,le.lastUpdate
                from LinkEntity le
                where le.lastUpdate is null  or le.lastUpdate < :time
                """, LinkEntity.class).
            setParameter("time", differenceTime)
            .getResultList();

        return entityList.stream().map(MapperLinkDTOLinkEntity::entityToDto).toList();
    }
}
