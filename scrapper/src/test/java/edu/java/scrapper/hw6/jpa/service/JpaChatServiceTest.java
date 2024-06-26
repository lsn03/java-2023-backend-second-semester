package edu.java.scrapper.hw6.jpa.service;

import edu.java.domain.model.ChatDto;
import edu.java.exception.exception.UserAlreadyExistException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.database.TgChatService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("jpa")
public class JpaChatServiceTest extends IntegrationTest {
    private static final long TG_CHAT_ID = 1l;

    @Autowired
    private TgChatService tgChatService;

    @Test
    @Transactional
    @Rollback
    public void addFirstTime() {
        tgChatService.add(TG_CHAT_ID);
        var response = tgChatService.findAll();
        assertEquals(List.of(new ChatDto(TG_CHAT_ID, true)), response);
    }

    @Test
    @Transactional
    @Rollback
    public void testAddDuplicate() {

        tgChatService.add(TG_CHAT_ID);
        assertThrows(UserAlreadyExistException.class, () -> tgChatService.add(TG_CHAT_ID));

    }
    @Test
    @Transactional
    @Rollback
    public void testAddAfterRemove() {

        tgChatService.add(TG_CHAT_ID);
        tgChatService.remove(TG_CHAT_ID);

        tgChatService.add(TG_CHAT_ID);
        var response = tgChatService.findAll();
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
            tgChatService.add(elem);
        }

        var response = (tgChatService.findAll());

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

        tgChatService.add(TG_CHAT_ID);
        tgChatService.remove(TG_CHAT_ID);
        var response = (tgChatService.findAll());
        assertEquals(List.of(new ChatDto(TG_CHAT_ID, false)), response);
    }
}
