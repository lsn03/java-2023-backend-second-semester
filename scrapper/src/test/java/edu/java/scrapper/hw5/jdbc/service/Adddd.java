package edu.java.scrapper.hw5.jdbc.service;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.exception.exception.RepeatTrackException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.process.jdbc.JdbcLinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class Adddd extends IntegrationTest {
    @Autowired
    private JdbcLinkService jdbcLinkService;
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    private LinkDTO linkForAction;
    long tgChatId = 1;
    URI uri = URI.create("https://github.com/owner/repo/pull/1");

    @Test
    @Rollback
    public void testAddRepeatTrackException() {
        linkForAction = new LinkDTO();
        linkForAction.setTgChatId(tgChatId);
        linkForAction.setUri(uri);
        jdbcChatRepository.add(tgChatId);
        jdbcLinkService.add(linkForAction);

        assertThrows(RepeatTrackException.class, () -> jdbcLinkService.add(linkForAction));
        System.out.println("""
            gf
            """);
    }
}
