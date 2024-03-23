package edu.java.scrapper.hw6.jpa.repo;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.ChatRepository;
import edu.java.exception.exception.UserAlreadyExistException;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("jpa")
public class JpaTgChatRepositoryTest extends IntegrationTest {
    @Autowired
    private ChatRepository jpaChatRepository;

    @Test
    @Rollback
    @Transactional
    public void addFirstTime() {
        long chatId = 1234;
        var expected = new ChatDTO(chatId, true);

        assertDoesNotThrow(() -> jpaChatRepository.add(chatId));
        var response = jpaChatRepository.findAll();
        assertEquals(1, response.size());

        assertEquals(expected, response.getFirst());

    }

    @Test
    @Rollback
    @Transactional
    public void addDuplicateTest() {
        long chatId = 1234;

        jpaChatRepository.add(chatId);
        assertThrows(UserAlreadyExistException.class, () -> jpaChatRepository.add(chatId));

    }

    @Test
    @Rollback
    @Transactional
    public void removeTest() {
        long chatId = 1234;

        jpaChatRepository.add(chatId);
        jpaChatRepository.remove(chatId);
        var response = jpaChatRepository.findAll();
        assertEquals(1, response.size());
        assertFalse(response.getFirst().isActive());
        assertEquals(chatId, response.getFirst().getChatId());

    }

}
