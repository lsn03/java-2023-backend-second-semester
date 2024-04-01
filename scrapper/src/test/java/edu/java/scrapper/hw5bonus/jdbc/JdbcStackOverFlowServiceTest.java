package edu.java.scrapper.hw5bonus.jdbc;

import edu.java.domain.model.LinkDto;
import edu.java.domain.model.StackOverFlowAnswerDto;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.database.jdbc.JdbcLinkService;
import edu.java.service.database.jdbc.JdbcStackOverFlowService;
import edu.java.service.database.jdbc.JdbcTgChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JdbcStackOverFlowServiceTest extends IntegrationTest {
    @Autowired
    private JdbcStackOverFlowService jdbcStackOverFlowService;
    @Autowired
    private JdbcLinkService jdbcLinkService;
    @Autowired
    private JdbcTgChatService jdbcTgChatService;

    private static final long ANSWER_ID = 1l;
    private static final long TG_CHAT_ID = 1l;
    OffsetDateTime time = OffsetDateTime.of(2015, 1, 1, 1, 1, 1, 0, ZoneOffset.UTC);
    URI uri = URI.create(
        "https://stackoverflow.com/questions/4006772/cannot-delete-indents-nor-past-insertion-point/"
    );
    LinkDto linkDTO;
    StackOverFlowAnswerDto elem;

    @Test
    @Transactional
    @Rollback
    public void testAddSuccess() {
        prepareFill();
        List<StackOverFlowAnswerDto> listForAdd = List.of(elem);

        int cnt = jdbcStackOverFlowService.addAnswers(listForAdd);
        assertEquals(1, cnt);
        var response = jdbcStackOverFlowService.getAnswers(uri);
        assertEquals(listForAdd.getFirst().getAnswerId(), response.getFirst().getAnswerId());

    }

    @Test
    @Transactional
    @Rollback
    public void testAddException() {
        prepareFill();
        List<StackOverFlowAnswerDto> listForAdd = List.of(elem);

        jdbcStackOverFlowService.addAnswers(listForAdd);
        assertThrows(RecordAlreadyExistException.class, () -> jdbcStackOverFlowService.addAnswers(listForAdd));

    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveSuccess() {
        prepareFill();
        List<StackOverFlowAnswerDto> listForAdd = List.of(elem);

        int cnt = jdbcStackOverFlowService.addAnswers(listForAdd);

        int deleted = jdbcStackOverFlowService.deleteAnswers(listForAdd);
        assertEquals(cnt, deleted);

    }

    private void prepareFill() {
        jdbcTgChatService.add(TG_CHAT_ID);
        linkDTO = new LinkDto(
            uri,
            TG_CHAT_ID,
            null,
            time,
            time
        );

        jdbcLinkService.add(linkDTO);
        elem = new StackOverFlowAnswerDto(
            linkDTO.getLinkId(),
            ANSWER_ID,
            "jon",
            false,
            time,
            time,
            time
        );
    }
}
