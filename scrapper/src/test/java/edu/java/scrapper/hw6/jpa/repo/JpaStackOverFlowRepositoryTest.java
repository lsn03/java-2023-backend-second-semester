package edu.java.scrapper.hw6.jpa.repo;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.model.StackOverFlowAnswerDTO;
import edu.java.domain.repository.LinkRepository;
import edu.java.domain.repository.StackOverFlowRepository;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.LocalDateTime;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("jpa")
public class JpaStackOverFlowRepositoryTest extends IntegrationTest {
    @Autowired
    private StackOverFlowRepository stackOverFlowRepository;
    @Autowired
    private LinkRepository linkRepository;
    URI uri = URI.create("https://example.com");
    OffsetDateTime time = LocalDateTime.now().atOffset(ZoneOffset.UTC);

    @Rollback
    @Test
    @Transactional
    public void addAnswers() {
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setTgChatId(1l);
        linkDTO.setUri(uri);
        linkRepository.add(linkDTO);

        StackOverFlowAnswerDTO elem1 = new StackOverFlowAnswerDTO(
            linkDTO.getLinkId(),
            1l,
            "jon",
            false,
            time,
            time,
            time
        );
        StackOverFlowAnswerDTO elem2 = new StackOverFlowAnswerDTO(
            linkDTO.getLinkId(),
            2l,
            "jon2",
            true,
            time,
            time,
            time
        );
        var listForAdd = List.of(elem1, elem2);
        int cnt = stackOverFlowRepository.addAnswers(listForAdd);
        assertTrue(cnt == 2);
        var responseByUri = stackOverFlowRepository.getAnswers(uri);
        assertEquals(listForAdd, responseByUri);
        var responseByLinkId = stackOverFlowRepository.getAnswers(linkDTO.getLinkId());
        assertEquals(listForAdd, responseByLinkId);
    }

    @Rollback
    @Test
    @Transactional
    public void deleteCommits() {
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setTgChatId(1l);
        linkDTO.setUri(uri);
        linkRepository.add(linkDTO);
        StackOverFlowAnswerDTO elem1 = new StackOverFlowAnswerDTO(
            linkDTO.getLinkId(),
            1l,
            "jon",
            false,
            time,
            time,
            time
        );
        StackOverFlowAnswerDTO elem2 = new StackOverFlowAnswerDTO(
            linkDTO.getLinkId(),
            2l,
            "jon2",
            true,
            time,
            time,
            time
        );
        var listForAdd = List.of(elem1, elem2);
        int cntAdded = stackOverFlowRepository.addAnswers(listForAdd);
        int cntRemoved = stackOverFlowRepository.deleteAnswers(listForAdd);
        assertEquals(cntAdded, cntRemoved);
        var response = stackOverFlowRepository.getAnswers(uri);
        assertTrue(response.isEmpty());

    }

    @Rollback
    @Test
    @Transactional
    public void addException() {
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setTgChatId(1l);
        linkDTO.setUri(uri);
        linkRepository.add(linkDTO);

        StackOverFlowAnswerDTO elem1 = new StackOverFlowAnswerDTO(
            linkDTO.getLinkId(),
            1l,
            "jon",
            false,
            time,
            time,
            time
        );
        StackOverFlowAnswerDTO elem2 = new StackOverFlowAnswerDTO(
            linkDTO.getLinkId(),
            2l,
            "jon2",
            true,
            time,
            time,
            time
        );
        var listForAdd = List.of(elem1, elem2);
        stackOverFlowRepository.addAnswers(listForAdd);
        assertThrows(RecordAlreadyExistException.class, () -> stackOverFlowRepository.addAnswers(listForAdd));

    }
}
