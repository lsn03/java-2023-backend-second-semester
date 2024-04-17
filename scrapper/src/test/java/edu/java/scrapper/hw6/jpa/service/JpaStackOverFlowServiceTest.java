package edu.java.scrapper.hw6.jpa.service;

import edu.java.domain.model.LinkDto;
import edu.java.domain.model.StackOverFlowAnswerDto;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.database.LinkService;
import edu.java.service.database.StackOverFlowService;
import edu.java.service.database.TgChatService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("jpa")
public class JpaStackOverFlowServiceTest extends IntegrationTest {
    @Autowired
    private StackOverFlowService stackOverFlowService;
    @Autowired
    private LinkService linkService;

    @Autowired
    private TgChatService tgChatService;
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

        int cnt = stackOverFlowService.addAnswers(listForAdd);
        assertEquals(1, cnt);
        var response = stackOverFlowService.getAnswers(uri);
        assertEquals(listForAdd.getFirst().getAnswerId(), response.getFirst().getAnswerId());

    }

    @Test
    @Transactional
    @Rollback
    public void testAddException() {
        prepareFill();
        List<StackOverFlowAnswerDto> listForAdd = List.of(elem);

        stackOverFlowService.addAnswers(listForAdd);
        assertThrows(RecordAlreadyExistException.class, () -> stackOverFlowService.addAnswers(listForAdd));

    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveSuccess() {
        prepareFill();
        List<StackOverFlowAnswerDto> listForAdd = List.of(elem);

        int cnt = stackOverFlowService.addAnswers(listForAdd);

        int deleted = stackOverFlowService.deleteAnswers(listForAdd);
        assertEquals(cnt, deleted);

    }

    private void prepareFill() {
        tgChatService.add(TG_CHAT_ID);
        linkDTO = new LinkDto(
            uri,
            TG_CHAT_ID,
            null,
            time,
            time
        );

        linkService.add(linkDTO);
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
