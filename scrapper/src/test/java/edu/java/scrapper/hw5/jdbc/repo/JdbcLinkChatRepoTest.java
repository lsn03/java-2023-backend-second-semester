package edu.java.scrapper.hw5.jdbc.repo;

import edu.java.configuration.access.JdbcAccessConfiguration;
import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.ChatRepository;
import edu.java.domain.repository.LinkChatRepository;
import edu.java.domain.repository.LinkRepository;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("jdbc")
public class JdbcLinkChatRepoTest extends IntegrationTest {

//    @Autowired
//    private JdbcAccessConfiguration jdbcAccessConfiguration;

    @Autowired
    private LinkChatRepository jdbcLinkChatRepository;
    @Autowired
    private ChatRepository jdbcChatRepository;
    @Autowired
    private LinkRepository jdbcLinkRepository;
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
