package edu.java.scrapper.hw6.jpa.repo;

import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.ChatRepository;
import edu.java.domain.repository.LinkChatRepository;
import edu.java.domain.repository.LinkRepository;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("jpa")
public class JpaLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private LinkChatRepository linkChatRepository;
    @Autowired
    private ChatRepository chatRepository;
    URI uri = URI.create("http://example.com");
    LinkDto linkDTO;

    @Test
    @Rollback
    @Transactional
    public void addFirstTime() {

        linkDTO = new LinkDto();

        linkDTO.setUri(uri);
        linkRepository.add(linkDTO);

        var response = linkRepository.findAll();
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(linkDTO, response.getFirst());

    }

    @Test
    @Rollback
    @Transactional
    public void removeTest() {

        linkDTO = new LinkDto();
        linkDTO.setTgChatId(1l);
        linkDTO.setUri(uri);
        chatRepository.add(1l);
        linkRepository.add(linkDTO);
        linkChatRepository.add(linkDTO);

        int cnt = linkChatRepository.remove(linkDTO);
        cnt += linkRepository.remove(linkDTO);
        assertTrue(cnt == 2);

        var response = linkRepository.findAll();
        assertTrue(response.isEmpty());

    }

    @Test
    @Rollback
    @Transactional
    public void removeForTwo() {

        linkDTO = new LinkDto();

        linkDTO.setUri(uri);
        linkDTO.setTgChatId(1l);
        chatRepository.add(1l);
        linkRepository.add(linkDTO);
        linkChatRepository.add(linkDTO);

        int cnt = linkRepository.remove(linkDTO);
        assertTrue(cnt == 0);

    }

    @Test
    @Rollback
    @Transactional
    public void updateLinkTest() {

        linkDTO = new LinkDto();
        linkDTO.setTgChatId(1l);
        linkDTO.setUri(uri);

        linkRepository.add(linkDTO);
        linkRepository.updateLink(linkDTO);

        var response = linkRepository.findAll().getFirst();

        assertNotNull(response.getLastUpdate());

    }

    @Test
    @Rollback
    @Transactional
    public void getAllOldLinks() {
        List<LinkDto> list = List.of(
            new LinkDto(URI.create("http://example.com/1"), 1l, null, null, OffsetDateTime.now()),
            new LinkDto(
                URI.create("http://example.com/2"),
                1l,
                null,
                null,
                OffsetDateTime.of(2015, 1, 1, 1, 1, 1, 0, ZoneOffset.UTC)
            ),
            new LinkDto(
                URI.create("http://example.com/3"),
                1l,
                null,
                null,
                OffsetDateTime.of(2020, 1, 1, 1, 1, 1, 0, ZoneOffset.UTC)
            ),
            new LinkDto(URI.create("http://example.com/4"), 1l, null, null, OffsetDateTime.now())
        );

        for (var ele : list) {
            linkRepository.add(ele);
        }
        var response = linkRepository.findAllOldLinks(10);
        assertTrue(response.size() == 2);
        var expectedList = List.of(URI.create("http://example.com/3"), URI.create("http://example.com/2"));
        var linksUri = response.stream().map(LinkDto::getUri).collect(Collectors.toSet());
        for (var elem : expectedList) {
            assertTrue(linksUri.contains(elem));
        }

    }
}
