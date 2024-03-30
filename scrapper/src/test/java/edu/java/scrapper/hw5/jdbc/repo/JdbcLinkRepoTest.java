package edu.java.scrapper.hw5.jdbc.repo;

import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.exception.exception.LinkNotFoundException;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JdbcLinkRepoTest extends IntegrationTest {
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    LinkDto linkDto;

    @Test
    @Rollback
    @Transactional
    public void addFirstTime() {

        linkDto = new LinkDto();
        linkDto.setUri(URI.create("http://example.com"));
        jdbcLinkRepository.add(linkDto);

        var response = jdbcLinkRepository.findAll();
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(linkDto, response.getFirst());

    }

    @Test
    @Rollback
    @Transactional
    public void addSecondTime() {

        linkDto = new LinkDto();
        linkDto.setUri(URI.create("http://example.com"));
        jdbcLinkRepository.add(linkDto);
        assertThrows(DuplicateKeyException.class, () -> jdbcLinkRepository.add(linkDto));

    }

    @Test
    @Rollback
    @Transactional
    public void removeTest() {
        linkDto = new LinkDto();
        linkDto.setUri(URI.create("http://example.com"));
        jdbcLinkRepository.add(linkDto);
        assertNotNull(linkDto.getLinkId());
        int affect = jdbcLinkRepository.remove(linkDto);
        assertTrue(affect != 0);
        var response = jdbcLinkRepository.findAll();
        assertTrue(response.isEmpty());
    }

    @Test
    @Rollback
    @Transactional
    public void removeNotExistLinkTest() {
        linkDto = new LinkDto();
        linkDto.setUri(URI.create("http://example.com"));

        assertThrows( LinkNotFoundException.class, ()-> jdbcLinkRepository.remove(linkDto));

    }
}
