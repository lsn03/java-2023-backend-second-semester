package edu.java.scrapper.hw5bonus.jooq;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.model.StackOverFlowAnswerDTO;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.database.jooq.JooqLinkService;
import edu.java.service.database.jooq.JooqStackOverFlowService;
import edu.java.service.database.jooq.JooqTgChatService;
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
public class JooqStackOverFlowServiceTest extends IntegrationTest {
    @Autowired
    private JooqStackOverFlowService jooqStackOverFlowService;
    @Autowired
    private JooqLinkService jooqLinkService;
    @Autowired
    private JooqTgChatService jooqTgChatService;

    public static final long ANSWER_ID = 1l;
    public static final long TG_CHAT_ID = 1l;
    OffsetDateTime time = OffsetDateTime.of(2015, 1, 1, 1, 1, 1, 0, ZoneOffset.UTC);
    URI uri = URI.create(
        "https://stackoverflow.com/questions/4006772/cannot-delete-indents-nor-past-insertion-point/"
    );
    LinkDTO linkDTO;
    StackOverFlowAnswerDTO elem;

    @Test
    @Transactional
    @Rollback
    public void testAddSuccess() {
        prepareFill();
        List<StackOverFlowAnswerDTO> listForAdd = List.of(elem);

        int cnt = jooqStackOverFlowService.addAnswers(listForAdd);
        assertEquals(1, cnt);
        var response = jooqStackOverFlowService.getAnswers(uri);
        assertEquals(listForAdd.getFirst().getAnswerId(), response.getFirst().getAnswerId());

    }

    @Test
    @Transactional
    @Rollback
    public void testAddException() {
        prepareFill();
        List<StackOverFlowAnswerDTO> listForAdd = List.of(elem);

        jooqStackOverFlowService.addAnswers(listForAdd);
        assertThrows(RecordAlreadyExistException.class, () -> jooqStackOverFlowService.addAnswers(listForAdd));

    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveSuccess() {
        prepareFill();
        List<StackOverFlowAnswerDTO> listForAdd = List.of(elem);

        int cnt = jooqStackOverFlowService.addAnswers(listForAdd);

        int deleted = jooqStackOverFlowService.deleteAnswers(listForAdd);
        assertEquals(cnt, deleted);

    }

    private void prepareFill() {
        jooqTgChatService.add(TG_CHAT_ID);
        linkDTO = new LinkDTO(
            uri,
            TG_CHAT_ID,
            null,
            time,
            time
        );

        jooqLinkService.add(linkDTO);
        elem = new StackOverFlowAnswerDTO(
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
