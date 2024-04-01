package edu.java.domain.repository.jooq;

import edu.java.domain.jooq.tables.Link;
import edu.java.domain.jooq.tables.LinkChat;
import edu.java.domain.jooq.tables.records.LinkRecord;
import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.LinkRepository;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository

public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dslContext;
    private final JooqLinkChatRepository jooqLinkChatRepository;

    @Override
    public LinkDTO add(LinkDTO linkDTO) {
        LinkRecord linkRecord = dslContext.insertInto(
                Link.LINK,
                Link.LINK.URI,
                Link.LINK.CREATED_AT

            )
            .values(linkDTO.getUri().toString(), linkDTO.getCreatedAt().toLocalDateTime())
            .returning(Link.LINK.LINK_ID)
            .fetchOne();
        Objects.requireNonNull(linkRecord);
        linkDTO.setLinkId(linkRecord.getValue(Link.LINK.LINK_ID));
        return linkDTO;

    }

    @Override
    public Integer remove(LinkDTO linkDTO) {
        return dslContext
            .delete(Link.LINK)
            .where(Link.LINK.URI.eq(linkDTO.getUri().toString()))
            .andNotExists(

                dslContext.selectOne()
                    .from(LinkChat.LINK_CHAT)
                    .where(LinkChat.LINK_CHAT.LINK_ID.eq(Link.LINK.LINK_ID))
            )
            .execute();
    }

    @Override
    public List<LinkDTO> findAllByChatId(Long tgChatId) {

        return jooqLinkChatRepository.findAllByChatId(tgChatId);
    }

    @Override
    public List<LinkDTO> findAllByLinkId(Long linkId) {
        return jooqLinkChatRepository.findAllByLinkId(linkId);
    }

    @Override
    public Long findLinkIdByUrl(URI uri) {
        return dslContext.select(Link.LINK.LINK_ID)
            .from(Link.LINK)
            .where(Link.LINK.URI.eq(uri.toString()))
            .fetchOneInto(Long.class);
    }

    @Override
    public List<LinkDTO> findAll() {
        return dslContext.selectFrom(Link.LINK)
            .fetchInto(LinkDTO.class);

    }

    @Override
    public void updateLink(LinkDTO elem) {
        dslContext.update(Link.LINK)
            .set(Link.LINK.URI, elem.getUri().toString())
            .set(Link.LINK.LAST_UPDATE, LocalDateTime.now())
            .where(Link.LINK.LINK_ID.eq(elem.getLinkId()))
            .execute();
    }

    @Override
    public List<LinkDTO> findAllOldLinks(Integer timeInSeconds) {
        return dslContext.selectDistinct(
                Link.LINK.LINK_ID,
                Link.LINK.URI,
                Link.LINK.CREATED_AT,
                Link.LINK.LAST_UPDATE
            ).from(Link.LINK)
            .where(DSL.condition(
                    "last_update IS NULL OR last_update < NOW() - INTERVAL '{0} seconds'",
                    timeInSeconds
                )
            ).fetch(recordLinkDTO -> {
                LinkDTO linkDTO = new LinkDTO();

                linkDTO.setLinkId(recordLinkDTO.get(Link.LINK.LINK_ID));
                linkDTO.setUri(URI.create(recordLinkDTO.get(Link.LINK.URI)));
                linkDTO.setCreatedAt(recordLinkDTO.get(Link.LINK.CREATED_AT).atOffset(ZoneOffset.UTC));

                LocalDateTime lastUpdate = recordLinkDTO.get(Link.LINK.LAST_UPDATE);
                if (lastUpdate != null) {
                    linkDTO.setLastUpdate(lastUpdate.atOffset(ZoneOffset.UTC));
                }

                return linkDTO;
            });
    }
}
