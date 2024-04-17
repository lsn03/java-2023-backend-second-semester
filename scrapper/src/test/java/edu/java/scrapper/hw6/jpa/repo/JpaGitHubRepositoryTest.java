package edu.java.scrapper.hw6.jpa.repo;

import edu.java.domain.model.GitHubCommitDto;
import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.GitHubRepository;
import edu.java.domain.repository.LinkRepository;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.LocalDateTime;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("jpa")
public class JpaGitHubRepositoryTest extends IntegrationTest {
    @Autowired
    private GitHubRepository gitHubRepository;
    @Autowired
    private LinkRepository linkRepository;
    URI uri = URI.create("https://example.com");

    @Rollback
    @Test
    @Transactional
    public void addCommits() {
        LinkDto linkDTO = new LinkDto();
        linkDTO.setTgChatId(1l);
        linkDTO.setUri(uri);
        linkRepository.add(linkDTO);
        var commitsList = List.of(
            new GitHubCommitDto(null, linkDTO.getLinkId(), "shashasha1", "author1", LocalDateTime.now().atOffset(
                ZoneOffset.UTC), "message"),
            new GitHubCommitDto(
                null,
                linkDTO.getLinkId(),
                "shashasha2",
                "author2",
                LocalDateTime.now().atOffset(ZoneOffset.UTC),
                "message2"
            )
        );
        LinkDto linkDTO2 = new LinkDto();
        linkDTO2.setTgChatId(25l);
        linkDTO2.setUri(URI.create("https://example.com/2"));
        linkRepository.add(linkDTO2);
        gitHubRepository.addCommits(List.of(new GitHubCommitDto(
            null,
            linkDTO2.getLinkId(),
            "shashasha233",
            "author23",
            LocalDateTime.now().atOffset(ZoneOffset.UTC),
            "message23"
        )));

        int cnt = gitHubRepository.addCommits(commitsList);
        assertEquals(2, cnt);

        var responseByLinkId = gitHubRepository.getCommits(linkDTO.getLinkId());

        var responseByUri = gitHubRepository.getCommits(uri);
        assertEquals(commitsList, responseByUri);
        assertEquals(commitsList, responseByLinkId);

    }

    @Rollback
    @Test
    @Transactional
    public void deleteCommits() {
        LinkDto linkDTO = new LinkDto();
        linkDTO.setTgChatId(1l);
        linkDTO.setUri(uri);
        linkRepository.add(linkDTO);
        var commitsList = List.of(
            new GitHubCommitDto(null, linkDTO.getLinkId(), "shashasha1", "author1", LocalDateTime.now().atOffset(
                ZoneOffset.UTC), "message"),
            new GitHubCommitDto(
                null,
                linkDTO.getLinkId(),
                "shashasha2",
                "author2",
                LocalDateTime.now().atOffset(ZoneOffset.UTC),
                "message2"
            )
        );
        int cntAdded = gitHubRepository.addCommits(commitsList);
        int cntRemoved = gitHubRepository.deleteCommits(commitsList);
        assertEquals(cntAdded, cntRemoved);
        var response = gitHubRepository.getCommits(uri);
        assertTrue(response.isEmpty());

    }

    @Rollback
    @Test
    @Transactional
    public void addCommitsException() {
        LinkDto linkDTO = new LinkDto();
        linkDTO.setTgChatId(1l);
        linkDTO.setUri(uri);
        linkRepository.add(linkDTO);
        var commitsList = List.of(
            new GitHubCommitDto(null, linkDTO.getLinkId(), "shashasha1", "author1", LocalDateTime.now().atOffset(
                ZoneOffset.UTC), "message"),
            new GitHubCommitDto(
                null,
                linkDTO.getLinkId(),
                "shashasha2",
                "author2",
                LocalDateTime.now().atOffset(ZoneOffset.UTC),
                "message2"
            )
        );

        gitHubRepository.addCommits(commitsList);
        assertThrows(RecordAlreadyExistException.class, () -> gitHubRepository.addCommits(commitsList));

    }

}
