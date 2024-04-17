package edu.java.scrapper.hw6.jpa.repo;

import edu.java.domain.model.LinkDto;
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
@ActiveProfiles("jpa")
public class JpaLinkChatRepositoryTest extends IntegrationTest {
    public static final URI uri = URI.create("https://exmaple.com");
    @Autowired
    private LinkChatRepository linkChatRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private LinkRepository linkRepository;
    long chatId = 1l;
    LinkDto linkDTO;

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        linkDTO = new LinkDto();
        linkDTO.setUri(uri);
        linkDTO.setTgChatId(chatId);
        chatRepository.add(chatId);
        linkRepository.add(linkDTO);

        assertDoesNotThrow(() -> linkChatRepository.add(linkDTO));
        var allByChat = linkChatRepository.findAllByChatId(chatId);
        var allByLink = linkChatRepository.findAllByLinkId(linkDTO.getLinkId());

        assertFalse(allByChat.isEmpty());
        assertFalse(allByLink.isEmpty());
        assertEquals(1, allByChat.size());
        assertEquals(1, allByLink.size());
    }

    @Test
    @Transactional
    @Rollback
    public void addDuplicateTest() {
        linkDTO = new LinkDto();
        linkDTO.setUri(uri);
        linkDTO.setTgChatId(chatId);
        chatRepository.add(chatId);
        linkRepository.add(linkDTO);

        assertDoesNotThrow(() -> linkChatRepository.add(linkDTO));
        var allByChat = linkChatRepository.findAllByChatId(chatId);
        var allByLink = linkChatRepository.findAllByLinkId(linkDTO.getLinkId());

        assertFalse(allByChat.isEmpty());
        assertFalse(allByLink.isEmpty());
        assertEquals(1, allByChat.size());
        assertEquals(1, allByLink.size());
    }

    @Test
    @Transactional
    @Rollback
    public void removeTest() {
        linkDTO = new LinkDto();
        linkDTO.setUri(uri);
        linkDTO.setTgChatId(chatId);
        chatRepository.add(chatId);
        linkRepository.add(linkDTO);

        assertDoesNotThrow(() -> linkChatRepository.remove(linkDTO));
        var allByChat = linkChatRepository.findAllByChatId(chatId);
        var allByLink = linkChatRepository.findAllByLinkId(linkDTO.getLinkId());

        assertTrue(allByChat.isEmpty());
        assertTrue(allByLink.isEmpty());

    }
}
