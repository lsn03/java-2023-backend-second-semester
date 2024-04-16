package edu.java.scrapper.hw5bonus.jooq;

import edu.java.domain.model.GitHubCommitDto;
import edu.java.domain.model.LinkDto;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.database.jooq.JooqGitHubService;
import edu.java.service.database.jooq.JooqLinkService;
import edu.java.service.database.jooq.JooqTgChatService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JooqGitHubServiceTest extends IntegrationTest {
    @Autowired
    private JooqGitHubService jooqGitHubService;
    @Autowired
    private JooqLinkService jooqLinkService;
    @Autowired
    private JooqTgChatService jooqTgChatService;


    private static final long TG_CHAT_ID = 1l;
    OffsetDateTime time = OffsetDateTime.of(2015, 1, 1, 1, 1, 1, 0, ZoneOffset.UTC);
    URI uri = URI.create(
        "https://github.com/lsn03/java-2023-backend-second-semester/pull/5"
    );
    GitHubCommitDto elem;
    LinkDto linkDTO;

    @Test
    @Transactional
    @Rollback
    public void testAddSuccess() {
        prepareFill();
        List<GitHubCommitDto> listForAdd = List.of(
            elem
        );
        int cnt = jooqGitHubService.addCommits(listForAdd);
        assertEquals(1, cnt);
        var response = jooqGitHubService.getCommits(uri);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(listForAdd.getFirst().getSha(), response.getFirst().getSha());
    }

    @Test
    @Transactional
    @Rollback
    public void testAddException() {
        prepareFill();
        List<GitHubCommitDto> listForAdd = List.of(elem);
        jooqGitHubService.addCommits(listForAdd);
        assertThrows(RecordAlreadyExistException.class, () -> jooqGitHubService.addCommits(listForAdd));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemove() {
        prepareFill();
        List<GitHubCommitDto> listForAdd = List.of(
            elem
        );
        int cnt = jooqGitHubService.addCommits(listForAdd);

        int deleted = jooqGitHubService.deleteCommits(listForAdd);
        assertEquals(cnt, deleted);
    }

    private void prepareFill() {
        jooqTgChatService.add(TG_CHAT_ID);
        linkDTO = new LinkDto(
            uri,
            TG_CHAT_ID,
            null,
            time,
            time
        );
        jooqLinkService.add(linkDTO);
        elem = new GitHubCommitDto(
            1l,
            linkDTO.getLinkId(),
            "shashasha",
            "author",
            time,
            "message"
        );
    }
}
