package edu.java.scrapper.hw5.jdbc.service;

import edu.java.exception.exception.UserAlreadyExistException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.database.TgChatService;
import edu.java.service.database.jdbc.JdbcTgChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("jdbc")
public class JdbcChatServiceTest extends IntegrationTest {
    public static final long TG_CHAT_ID = 1l;
    @Autowired
    private TgChatService jdbcTgChatService;

    @Test
    @Rollback
    @Transactional
    public void addSuccess(){
        jdbcTgChatService.add(TG_CHAT_ID);
        var result = jdbcTgChatService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.getFirst().getChatId());
        assertTrue( result.getFirst().isActive());
    }

    @Test
    @Rollback
    @Transactional
    public void addSuccessFailed(){
        jdbcTgChatService.add(TG_CHAT_ID);
        assertThrows(UserAlreadyExistException.class,() -> jdbcTgChatService.add(TG_CHAT_ID));

    }
    @Test
    @Rollback
    @Transactional
    public void remove(){
        jdbcTgChatService.add(TG_CHAT_ID);
        jdbcTgChatService.remove(TG_CHAT_ID);
        var result = jdbcTgChatService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.getFirst().getChatId());
        assertFalse( result.getFirst().isActive());
    }
}
