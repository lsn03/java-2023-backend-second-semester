package edu.java.scrapper.hw5.jdbc.service;

import edu.java.domain.repository.ChatRepository;
import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.exception.exception.ListEmptyException;
import edu.java.exception.exception.RepeatTrackException;
import edu.java.exception.exception.UserDoesntExistException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.database.LinkService;
import edu.java.service.database.jdbc.JdbcLinkService;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("jdbc")
public class JdbcLinkServiceTest extends IntegrationTest {
    @Autowired
    private LinkService jdbcLinkService;
    @Autowired
    private ChatRepository jdbcChatRepository;

    private LinkDto linkForAction;
    long tgChatId = 1;
    URI uri = URI.create("https://github.com/owner/repo/pull/1");
    @Mock
    JdbcLinkService mockService;

    @Test
    @Rollback
    @Transactional
    public void testAddSuccess() {
        linkForAction = new LinkDto();
        linkForAction.setTgChatId(tgChatId);
        linkForAction.setUri(uri);
        jdbcChatRepository.add(tgChatId);
        jdbcLinkService.add(linkForAction);

        var result = jdbcLinkService.findAll(tgChatId);
        var actual = result.getFirst();

        assertEquals(1, result.size());
        assertEquals(1, actual.getTgChatId());
        assertEquals(uri, actual.getUri());

    }

    @Test
    @Rollback
    @Transactional
    public void testAddForNotExistUser() {
        linkForAction = new LinkDto();
        linkForAction.setTgChatId(tgChatId);
        linkForAction.setUri(uri);
        assertThrows(UserDoesntExistException.class, () -> jdbcLinkService.add(linkForAction));
    }

    @Test
    @Rollback
    @Transactional
    public void testAddRepeatTrackException() {
        linkForAction = new LinkDto();
        linkForAction.setTgChatId(tgChatId);
        linkForAction.setUri(uri);
        Mockito.when(mockService.add(linkForAction)).thenThrow(RepeatTrackException.class);

        assertThrows(RepeatTrackException.class, () -> mockService.add(linkForAction));
    }

    @Test
    @Rollback
    @Transactional
    public void testFindLinksFailed() {
        assertThrows(ListEmptyException.class, () -> jdbcLinkService.findAll(tgChatId));
    }

    @Test
    @Rollback
    @Transactional
    public void testRemoveSuccess() {
        linkForAction = new LinkDto();
        linkForAction.setTgChatId(tgChatId);
        linkForAction.setUri(uri);
        jdbcChatRepository.add(tgChatId);
        jdbcLinkService.add(linkForAction);

        Integer cnt = jdbcLinkService.remove(linkForAction);

        assertEquals(2, cnt);

    }
}
