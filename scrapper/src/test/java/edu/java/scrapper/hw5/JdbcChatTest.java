package edu.java.scrapper.hw5;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JdbcChatTest extends IntegrationTest {
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

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
        var expected = new ChatDTO(chatId, true);

        assertDoesNotThrow(() -> jdbcChatRepository.add(chatId));
        var response = jdbcChatRepository.findAll().get(0);

        assertEquals(expected, response);

    }
}
