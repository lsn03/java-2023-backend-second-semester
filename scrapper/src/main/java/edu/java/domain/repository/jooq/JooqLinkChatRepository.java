package edu.java.domain.repository.jooq;

import edu.java.domain.jooq.tables.Link;
import edu.java.domain.jooq.tables.LinkChat;
import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.LinkChatRepository;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository
@RequiredArgsConstructor
public class JooqLinkChatRepository implements LinkChatRepository {
    private final DSLContext dslContext;

    @Override
    @Transactional
    public void add(LinkDTO linkDTO) {
        dslContext.insertInto(LinkChat.LINK_CHAT, LinkChat.LINK_CHAT.LINK_ID, LinkChat.LINK_CHAT.CHAT_ID)
            .values(linkDTO.getLinkId(), linkDTO.getTgChatId())
            .execute();
    }

    @Override
    @Transactional
    public Integer remove(LinkDTO linkDTO) {
        return dslContext.deleteFrom(LinkChat.LINK_CHAT)
            .where(LinkChat.LINK_CHAT.LINK_ID.eq(linkDTO.getLinkId())
                .and(LinkChat.LINK_CHAT.CHAT_ID.eq(linkDTO.getTgChatId())))
            .execute();
    }

    @Override
    @Transactional
    public Integer remove(Long tgChatId) {
        return dslContext.deleteFrom(LinkChat.LINK_CHAT)
            .where(LinkChat.LINK_CHAT.CHAT_ID.eq(tgChatId))
            .execute();
    }

    @Override
    @Transactional
    public List<LinkDTO> findAllByChatId(Long tgChatId) {
        return dslContext.select(Link.LINK.LINK_ID, Link.LINK.URI, Link.LINK.SITE_TYPE_ID)
            .from(Link.LINK)
            .innerJoin(LinkChat.LINK_CHAT).on(LinkChat.LINK_CHAT.LINK_ID.eq(Link.LINK.LINK_ID))
            .where(LinkChat.LINK_CHAT.CHAT_ID.eq(tgChatId))
            .fetch(recordLinkDTO -> {
                LinkDTO linkDTO = new LinkDTO();
                linkDTO.setUri(URI.create(recordLinkDTO.getValue(Link.LINK.URI)));
                linkDTO.setTgChatId((recordLinkDTO.getValue(LinkChat.LINK_CHAT.CHAT_ID)));
                linkDTO.setLinkId(recordLinkDTO.getValue(Link.LINK.LINK_ID));
                linkDTO.setSiteTypeId(recordLinkDTO.getValue(Link.LINK.SITE_TYPE_ID).intValue());
                return linkDTO;
            });
    }

    @Override
    @Transactional
    public List<LinkDTO> findAllByLinkId(Long linkId) {
        return dslContext.select(Link.LINK.LINK_ID, Link.LINK.URI, Link.LINK.SITE_TYPE_ID)
            .from(Link.LINK)
            .innerJoin(LinkChat.LINK_CHAT).on(LinkChat.LINK_CHAT.LINK_ID.eq(Link.LINK.LINK_ID))
            .where(LinkChat.LINK_CHAT.LINK_ID.eq(linkId))
            .fetch(recordLinkDTO -> {
                LinkDTO linkDTO = new LinkDTO();
                linkDTO.setUri(URI.create(recordLinkDTO.getValue(Link.LINK.URI)));
                linkDTO.setTgChatId((recordLinkDTO.getValue(LinkChat.LINK_CHAT.CHAT_ID)));
                linkDTO.setSiteTypeId(recordLinkDTO.getValue(Link.LINK.SITE_TYPE_ID).intValue());
                linkDTO.setLinkId(linkId);
                return linkDTO;
            });
    }
}
