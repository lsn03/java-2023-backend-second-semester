package edu.java.scrapper.hw6.jpa.service;

import edu.java.domain.model.LinkDto;
import edu.java.exception.exception.IncorrectParametersException;
import edu.java.exception.exception.ListEmptyException;
import edu.java.exception.exception.RepeatTrackException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.database.LinkService;
import edu.java.service.database.TgChatService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("jpa")
public class JpaLinkServiceTest extends IntegrationTest {
    private static final long TG_CHAT_ID = 1l;
    @Autowired
    private TgChatService tgChatService;
    @Autowired
    private LinkService linkService;
    URI uri = URI.create("https://github.com/lsn03/java-2023-backend-second-semester/pull/5");

    OffsetDateTime createdAt = OffsetDateTime.of(2015, 1, 1, 1, 1, 1, 0, ZoneOffset.UTC);
    private LinkDto linkDTO;

    @Test
    @Transactional
    @Rollback
    public void testAddFirstTimeSuccess() {
        linkDTO = new LinkDto();
        linkDTO.setUri(uri);
        linkDTO.setTgChatId(TG_CHAT_ID);
        linkDTO.setCreatedAt(createdAt);
        tgChatService.add(TG_CHAT_ID);

        linkService.add(linkDTO);
        assertNotNull(linkDTO.getLinkId());
        var list = linkService.findAll(TG_CHAT_ID);
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
        tgChatService.add(TG_CHAT_ID);

        linkService.add(linkDTO);
        assertThrows(RepeatTrackException.class, () -> linkService.add(linkDTO));

    }

    @Test
    @Transactional
    @Rollback
    public void testAddInvalidLink() {
        linkDTO = new LinkDto();
        linkDTO.setUri(URI.create("https://example.com"));
        linkDTO.setTgChatId(TG_CHAT_ID);
        linkDTO.setCreatedAt(createdAt);

        assertThrows(IncorrectParametersException.class, () -> linkService.add(linkDTO));

    }

    @Test
    @Transactional
    @Rollback
    public void testFindEmpty() {

        assertThrows(ListEmptyException.class, () -> linkService.findAll(TG_CHAT_ID));

    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveForOneChat() {
        linkDTO = new LinkDto();
        linkDTO.setUri(uri);
        linkDTO.setTgChatId(TG_CHAT_ID);
        linkDTO.setCreatedAt(createdAt);
        tgChatService.add(TG_CHAT_ID);
        linkService.add(linkDTO);

        int cnt = linkService.remove(linkDTO);
        assertTrue(cnt == 2);

        assertThrows(ListEmptyException.class, () -> linkService.findAll(TG_CHAT_ID));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveForTwoChats() {
        linkDTO = new LinkDto();
        linkDTO.setUri(uri);
        linkDTO.setTgChatId(TG_CHAT_ID);
        linkDTO.setCreatedAt(createdAt);
        tgChatService.add(TG_CHAT_ID);
        tgChatService.add(TG_CHAT_ID + 1);

        linkService.add(linkDTO);
        linkDTO.setLinkId(null);
        linkDTO.setTgChatId(TG_CHAT_ID + 1);
        linkService.add(linkDTO);

        int cnt = linkService.remove(linkDTO);
        assertEquals(1, cnt);

        var response = linkService.findAll(TG_CHAT_ID);
        assertFalse(response.isEmpty());
        var link = response.getFirst();
        assertEquals(TG_CHAT_ID,link.getTgChatId());

    }

}
