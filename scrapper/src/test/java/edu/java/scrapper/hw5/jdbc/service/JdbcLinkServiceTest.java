package edu.java.scrapper.hw5.jdbc.service;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.exception.exception.RepeatTrackException;
import edu.java.exception.exception.UserDoesntExistException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.process.jdbc.JdbcLinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JdbcLinkServiceTest extends IntegrationTest {
    @Autowired
    private JdbcLinkService jdbcLinkService;
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    private LinkDTO linkForAction;
    long tgChatId = 1;
    URI uri = URI.create("https://github.com/owner/repo/pull/1");

    @Test
    @Rollback
    @Transactional
    public void testAddSuccess() {
        linkForAction = new LinkDTO();
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
        linkForAction = new LinkDTO();
        linkForAction.setTgChatId(tgChatId);
        linkForAction.setUri(uri);

        assertThrows(UserDoesntExistException.class, () -> jdbcLinkService.add(linkForAction));

    }



    public void testAddRepeatTrackException2() {
//        linkForAction = new LinkDTO();
//        linkForAction.setTgChatId(tgChatId);
//        linkForAction.setUri(uri);
//        jdbcChatRepository.add(tgChatId);
//        jdbcLinkService.add(linkForAction);
//
//        linkForAction.setLinkId(null);
//        assertThrows(RepeatTrackException.class, () -> jdbcLinkService.add(linkForAction));

    }
}
