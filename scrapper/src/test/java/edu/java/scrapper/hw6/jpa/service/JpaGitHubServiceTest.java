package edu.java.scrapper.hw6.jpa.service;

import edu.java.domain.model.GitHubCommitDto;
import edu.java.domain.model.LinkDto;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.database.GitHubService;
import edu.java.service.database.LinkService;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("jpa")
public class JpaGitHubServiceTest extends IntegrationTest {
    @Autowired
    private GitHubService gitHubService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private TgChatService tgChatService;


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
        int cnt = gitHubService.addCommits(listForAdd);
        assertEquals(1, cnt);
        var response = gitHubService.getCommits(uri);
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
        gitHubService.addCommits(listForAdd);
        assertThrows(RecordAlreadyExistException.class, () -> gitHubService.addCommits(listForAdd));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemove() {
        prepareFill();
        List<GitHubCommitDto> listForAdd = List.of(
            elem
        );
        int cnt = gitHubService.addCommits(listForAdd);

        int deleted = gitHubService.deleteCommits(listForAdd);
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
        elem = new GitHubCommitDto(
            null,
            linkDTO.getLinkId(),
            "shashasha",
            "author",
            time,
            "message"
        );
    }
}
