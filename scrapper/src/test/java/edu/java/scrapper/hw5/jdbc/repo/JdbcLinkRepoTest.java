package edu.java.scrapper.hw5.jdbc.repo;

import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
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

    LinkDto linkDTO;

    @Test
    @Rollback
    @Transactional
    public void addFirstTime() {

        linkDTO = new LinkDto();

        linkDTO.setUri(URI.create("http://example.com"));
        jdbcLinkRepository.add(linkDTO);

        var response = jdbcLinkRepository.findAll();
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(linkDto, response.getFirst());

    }

    @Test
    @Rollback
    @Transactional
    public void addSecondTime() {

        linkDTO = new LinkDto();
        linkDTO.setUri(URI.create("http://example.com"));
        jdbcLinkRepository.add(linkDTO);
        assertThrows(DuplicateKeyException.class, () -> jdbcLinkRepository.add(linkDTO));

    }

    @Test
    @Rollback
    @Transactional
    public void removeTest() {
        linkDTO = new LinkDto();
        linkDTO.setUri(URI.create("http://example.com"));
        jdbcLinkRepository.add(linkDTO);
        assertNotNull(linkDTO.getLinkId());
        int affect = jdbcLinkRepository.remove(linkDTO);
        assertTrue(affect != 0);
        var response = jdbcLinkRepository.findAll();
        assertTrue(response.isEmpty());
    }


}
