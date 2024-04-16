package edu.java.scrapper.hw5bonus.jdbc;

import edu.java.domain.model.GitHubCommitDto;
import edu.java.domain.model.LinkDto;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.database.jdbc.JdbcGitHubService;
import edu.java.service.database.jdbc.JdbcLinkService;
import edu.java.service.database.jdbc.JdbcTgChatService;
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
public class JdbcGitHubServiceTest extends IntegrationTest {

    @Autowired
    private JdbcGitHubService jdbcGitHubService;
    @Autowired
    private JdbcLinkService jdbcLinkService;
    @Autowired
    private JdbcTgChatService jdbcTgChatService;

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
        int cnt = jdbcGitHubService.addCommits(listForAdd);
        assertEquals(1, cnt);
        var response = jdbcGitHubService.getCommits(uri);
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
        jdbcGitHubService.addCommits(listForAdd);
        assertThrows(RecordAlreadyExistException.class, () -> jdbcGitHubService.addCommits(listForAdd));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemove() {
        prepareFill();
        List<GitHubCommitDto> listForAdd = List.of(
            elem
        );
        int cnt = jdbcGitHubService.addCommits(listForAdd);

        int deleted = jdbcGitHubService.deleteCommits(listForAdd);
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
