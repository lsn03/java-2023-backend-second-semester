package edu.java.scrapper.hw5.jdbc.repo;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.ChatRepository;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("jdbc")
public class JdbcChatRepoTest extends IntegrationTest {
    @Autowired
    private ChatRepository jdbcChatRepository;

    @Test
    @Rollback
    @Transactional
    public void addFirstTime() {
        long chatId = 1234;
        var expected = new ChatDTO(chatId, true);

        assertDoesNotThrow(() -> jdbcChatRepository.add(chatId));
        var response = jdbcChatRepository.findAll().get(0);

        assertEquals(expected, response);

    }

    @Test
    @Rollback
    @Transactional
    public void addSecondTime() {
        long chatId = 1234;
        jdbcChatRepository.add(chatId);

        assertThrows(DuplicateKeyException.class, () -> jdbcChatRepository.add(chatId));

    }

    @Test
    @Transactional
    @Rollback
    public void removeTest() {
        long chatId = 1234;
        jdbcChatRepository.add(chatId);

        jdbcChatRepository.remove(chatId);
        var response = jdbcChatRepository.findAll();
        assertFalse(response.isEmpty());
        assertFalse(response.getFirst().isActive());

    }

    @Test
    @Transactional
    @Rollback
    public void addAfterRemove() {
        long chatId = 1234;
        jdbcChatRepository.add(chatId);

        jdbcChatRepository.remove(chatId);
        var response = jdbcChatRepository.findAll();
        jdbcChatRepository.add(chatId);

        response = jdbcChatRepository.findAll();

        assertFalse(response.isEmpty());
        assertTrue(response.getFirst().isActive());

    }
}
