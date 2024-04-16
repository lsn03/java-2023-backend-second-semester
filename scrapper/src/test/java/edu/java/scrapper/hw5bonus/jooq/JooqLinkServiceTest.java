package edu.java.scrapper.hw5bonus.jooq;

import edu.java.domain.model.LinkDto;
import edu.java.exception.exception.IncorrectParametersException;
import edu.java.exception.exception.ListEmptyException;
import edu.java.exception.exception.RepeatTrackException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.database.jooq.JooqLinkService;
import edu.java.service.database.jooq.JooqTgChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JooqLinkServiceTest extends IntegrationTest {

    private static final long TG_CHAT_ID = 1l;
    @Autowired
    private JooqTgChatService jooqTgChatService;
    @Autowired
    private JooqLinkService jooqLinkService;
    URI uri = URI.create("https://github.com/lsn03/java-2023-backend-second-semester/pull/5");
    LinkDto linkDTO;
    OffsetDateTime createdAt = OffsetDateTime.of(2015, 1, 1, 1, 1, 1, 0, ZoneOffset.UTC);

    @Test
    @Transactional
    @Rollback
    public void testAddFirstTimeSuccess() {
        linkDTO = new LinkDto();
        linkDTO.setUri(uri);
        linkDTO.setTgChatId(TG_CHAT_ID);
        linkDTO.setCreatedAt(createdAt);
        jooqTgChatService.add(TG_CHAT_ID);

        jooqLinkService.add(linkDTO);
        assertNotNull(linkDTO.getLinkId());
        var list = jooqLinkService.findAll(TG_CHAT_ID);
        assertFalse(list.isEmpty());
        assertEquals(linkDTO, list.getFirst());
    }

    @Test
    @Transactional
    @Rollback
    public void testAddTwiceTime() {
        linkDTO = new LinkDto();
        linkDTO.setUri(uri);
        linkDTO.setTgChatId(TG_CHAT_ID);
        linkDTO.setCreatedAt(createdAt);
        jooqTgChatService.add(TG_CHAT_ID);

        jooqLinkService.add(linkDTO);
        assertThrows(RepeatTrackException.class, () -> jooqLinkService.add(linkDTO));

    }

    @Test
    @Transactional
    @Rollback
    public void testAddInvalidLink() {
        linkDTO = new LinkDto();
        linkDTO.setUri(URI.create("https://example.com"));
        linkDTO.setTgChatId(TG_CHAT_ID);
        linkDTO.setCreatedAt(createdAt);

        assertThrows(IncorrectParametersException.class, () -> jooqLinkService.add(linkDTO));

    }

    @Test
    @Transactional
    @Rollback
    public void testFindEmpty() {

        assertThrows(ListEmptyException.class, () -> jooqLinkService.findAll(TG_CHAT_ID));

    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveForOneChat() {
        linkDTO = new LinkDto();
        linkDTO.setUri(uri);
        linkDTO.setTgChatId(TG_CHAT_ID);
        linkDTO.setCreatedAt(createdAt);
        jooqTgChatService.add(TG_CHAT_ID);
        jooqLinkService.add(linkDTO);

        int cnt = jooqLinkService.remove(linkDTO);
        assertTrue(cnt == 2);

        assertThrows(ListEmptyException.class, () -> jooqLinkService.findAll(TG_CHAT_ID));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveForTwoChats() {
        linkDTO = new LinkDto();
        linkDTO.setUri(uri);
        linkDTO.setTgChatId(TG_CHAT_ID);
        linkDTO.setCreatedAt(createdAt);
        jooqTgChatService.add(TG_CHAT_ID);
        jooqTgChatService.add(TG_CHAT_ID + 1);

        jooqLinkService.add(linkDTO);
        linkDTO.setLinkId(null);
        linkDTO.setTgChatId(TG_CHAT_ID + 1);
        jooqLinkService.add(linkDTO);

        int cnt = jooqLinkService.remove(linkDTO);
        assertEquals(1, cnt);

        var response = jooqLinkService.findAll(TG_CHAT_ID);
        assertFalse(response.isEmpty());
        var link = response.getFirst();
        assertEquals(TG_CHAT_ID,link.getTgChatId());

    }
}
