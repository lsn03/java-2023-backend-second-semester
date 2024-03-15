package edu.java.domain.repository.jooq;

import edu.java.domain.jooq.tables.Link;
import edu.java.domain.jooq.tables.LinkChat;
import edu.java.domain.jooq.tables.records.LinkRecord;
import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
@Primary
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dslContext;

    @Override
    public LinkDTO add(LinkDTO linkDTO) {
        LinkRecord record = dslContext.insertInto(Link.LINK, Link.LINK.URI, Link.LINK.CREATED_AT)
            .values(linkDTO.getUri().toString(), LocalDateTime.now())
            .returning(Link.LINK.LINK_ID)
            .fetchOne();
        Objects.requireNonNull(record);
        linkDTO.setLinkId(Long.valueOf(record.getValue(Link.LINK.LINK_ID)));
        return linkDTO;

    }

    @Override
    public Integer remove(LinkDTO linkDTO) {
        int deletedRecords = dslContext
            .delete(Link.LINK)
            .where(Link.LINK.URI.eq(linkDTO.getUri().toString()))
            .andNotExists(

                dslContext.selectOne()
                    .from(LinkChat.LINK_CHAT)
                    .where(LinkChat.LINK_CHAT.LINK_ID.eq(Link.LINK.LINK_ID))
            )
            .execute();
        return deletedRecords;
    }

    @Override
    public List<LinkDTO> findAllByChatId(Long tgChatId) {

        return null;
    }

    @Override
    public Long findUrl(URI uri) {
        Long id = dslContext.select(Link.LINK.LINK_ID)
            .from(Link.LINK)
            .where(Link.LINK.URI.eq(uri.toString()))
            .fetchOneInto(Long.class);
        return id;
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
            .where(Link.LINK.LINK_ID.eq(elem.getLinkId().intValue()))
            .execute();
    }

    @Override
    public List<LinkDTO> findAllOldLinks(Integer timeInSeconds) {
        return dslContext.select(
                Link.LINK.LINK_ID,
                Link.LINK.URI,
                Link.LINK.CREATED_AT,
                Link.LINK.LAST_UPDATE,
                LinkChat.LINK_CHAT.CHAT_ID
            ).from(Link.LINK)
            .leftJoin(LinkChat.LINK_CHAT)
            .on(Link.LINK.LINK_ID.eq(LinkChat.LINK_CHAT.LINK_ID))
            .where(DSL.condition(
                    "link.last_update IS NULL OR link.last_update < NOW() - INTERVAL '{0} second'",
                    timeInSeconds
                )
            ).fetch(record -> {
                LinkDTO linkDTO = new LinkDTO();

                linkDTO.setLinkId(Long.valueOf(record.get(Link.LINK.LINK_ID)));
                linkDTO.setUri(URI.create(record.get(Link.LINK.URI)));
                linkDTO.setCreatedAt(record.get(Link.LINK.CREATED_AT).atZone(ZoneId.systemDefault())
                    .toOffsetDateTime());

                LocalDateTime lastUpdate = record.get(Link.LINK.LAST_UPDATE);
                if (lastUpdate != null) {
                    linkDTO.setLastUpdate(lastUpdate.atZone(ZoneId.systemDefault()).toOffsetDateTime());
                } else {
                    linkDTO.setLastUpdate(OffsetDateTime.now());
                }

                linkDTO.setHash(record.get(Link.LINK.HASH));
                Long chatId = record.get(LinkChat.LINK_CHAT.CHAT_ID);

                linkDTO.setTgChatId(chatId);

                return linkDTO;
            });
    }
}
