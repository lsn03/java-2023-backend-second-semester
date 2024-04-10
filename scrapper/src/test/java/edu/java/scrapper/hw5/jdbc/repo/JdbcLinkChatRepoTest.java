package edu.java.scrapper.hw5.jdbc.repo;

import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JdbcLinkChatRepoTest extends IntegrationTest {

    @Autowired
    private JdbcLinkChatRepository jdbcLinkChatRepository;
    @Autowired
    private JdbcChatRepository jdbcChatRepository;
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;
    long chatId = 1l;
    LinkDto linkDto;

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        linkDto = new LinkDto();
        linkDto.setUri(URI.create("https://exmaple.com"));
        linkDto.setTgChatId(chatId);
        jdbcChatRepository.add(chatId);
        jdbcLinkRepository.add(linkDto);

        assertDoesNotThrow(() -> jdbcLinkChatRepository.add(linkDto));
        var allByChat = jdbcLinkChatRepository.findAllByChatId(chatId);
        var allByLink = jdbcLinkChatRepository.findAllByLinkId(linkDto.getLinkId());

        assertFalse(allByChat.isEmpty());
        assertFalse(allByLink.isEmpty());
        assertEquals(1, allByChat.size());
        assertEquals(1, allByLink.size());
    }

    @Test
    @Transactional
    @Rollback
    public void removeTest() {
        linkDto = new LinkDto();
        linkDto.setUri(URI.create("https://exmaple.com"));
        linkDto.setTgChatId(chatId);
        jdbcChatRepository.add(chatId);
        jdbcLinkRepository.add(linkDto);

        assertDoesNotThrow(() -> jdbcLinkChatRepository.remove(linkDto));
        var allByChat = jdbcLinkChatRepository.findAllByChatId(chatId);
        var allByLink = jdbcLinkChatRepository.findAllByLinkId(linkDto.getLinkId());

        assertTrue(allByChat.isEmpty());
        assertTrue(allByLink.isEmpty());

    }

}
