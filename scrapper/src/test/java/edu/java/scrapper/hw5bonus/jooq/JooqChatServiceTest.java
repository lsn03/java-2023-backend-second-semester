package edu.java.scrapper.hw5bonus.jooq;

import edu.java.domain.model.ChatDTO;
import edu.java.exception.exception.UserAlreadyExistException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.database.jooq.JooqTgChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JooqChatServiceTest extends IntegrationTest {
    private static final long TG_CHAT_ID = 1l;
    @Autowired
    private JooqTgChatService jooqTgChatService;

    @Test
    @Transactional
    @Rollback
    public void testAddFirstTimeSuccess() {

        jooqTgChatService.add(TG_CHAT_ID);
        var response = (jooqTgChatService.findAll());
        assertEquals(List.of(new ChatDTO(TG_CHAT_ID, true)), response);
    }

    @Test
    @Transactional
    @Rollback
    public void testAddDuplicate() {

        jooqTgChatService.add(TG_CHAT_ID);
        assertThrows(UserAlreadyExistException.class, () -> jooqTgChatService.add(TG_CHAT_ID));

    }

    @Test
    @Transactional
    @Rollback
    public void testAddAfterRemove() {

        jooqTgChatService.add(TG_CHAT_ID);
        jooqTgChatService.remove(TG_CHAT_ID);

        jooqTgChatService.add(TG_CHAT_ID);
        var response = jooqTgChatService.findAll();
        var elem = response.getFirst();
        assertEquals(1, elem.getChatId());
        assertTrue(elem.isActive());
    }

    @Test
    @Transactional
    @Rollback
    public void testAddManyUsersSuccess() {

        var list = List.of(1l, 2l, 3l, 4l);
        for (var elem : list) {
            jooqTgChatService.add(elem);
        }

        var response = (jooqTgChatService.findAll());

        assertEquals(list.size(), response.size());
        int index = 0;
        for (var elem : response) {
            assertEquals(list.get(index), elem.getChatId());
            assertTrue(elem.isActive());
            index++;
        }

    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveFirstTimeSuccess() {

        jooqTgChatService.add(TG_CHAT_ID);
        jooqTgChatService.remove(TG_CHAT_ID);
        var response = (jooqTgChatService.findAll());
        assertEquals(List.of(new ChatDTO(TG_CHAT_ID, false)), response);
    }
}
