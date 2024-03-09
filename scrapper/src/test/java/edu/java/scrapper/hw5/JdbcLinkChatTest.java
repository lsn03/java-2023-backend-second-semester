package edu.java.scrapper.hw5;

import edu.java.domain.model.LinkDTO;
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
public class JdbcLinkChatTest extends IntegrationTest {

    @Autowired
    private JdbcLinkChatRepository jdbcLinkChatRepository;
    @Autowired
    private JdbcChatRepository jdbcChatRepository;
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;
    long chatId = 1l;
    LinkDTO linkDTO;

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        linkDTO = new LinkDTO();
        linkDTO.setUri(URI.create("https://exmaple.com"));
        linkDTO.setTgChatId(chatId);
        jdbcChatRepository.add(chatId);
        jdbcLinkRepository.add(linkDTO);

        assertDoesNotThrow(() -> jdbcLinkChatRepository.add(linkDTO));
        var allByChat = jdbcLinkChatRepository.findAllByChatId(chatId);
        var allByLink = jdbcLinkChatRepository.findAllByLinkId(linkDTO.getLinkId());

        assertFalse(allByChat.isEmpty());
        assertFalse(allByLink.isEmpty());
        assertEquals(1, allByChat.size());
        assertEquals(1, allByLink.size());
    }

    @Test
    @Transactional
    @Rollback
    public void removeTest() {
        linkDTO = new LinkDTO();
        linkDTO.setUri(URI.create("https://exmaple.com"));
        linkDTO.setTgChatId(chatId);
        jdbcChatRepository.add(chatId);
        jdbcLinkRepository.add(linkDTO);

        assertDoesNotThrow(() -> jdbcLinkChatRepository.remove(linkDTO));
        var allByChat = jdbcLinkChatRepository.findAllByChatId(chatId);
        var allByLink = jdbcLinkChatRepository.findAllByLinkId(linkDTO.getLinkId());

        assertTrue(allByChat.isEmpty());
        assertTrue(allByLink.isEmpty());

    }

}
