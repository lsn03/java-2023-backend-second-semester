package edu.java.scrapper.hw4;

import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
public class MigrationTest extends IntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String uri = "http://example.com";

    @Test
    @Transactional
    @Rollback
    public void testAddChat() {
        int cnt = jdbcTemplate.update("insert into chat values (?)", 1l);
        assertTrue(cnt == 1);
        List<Integer> count = jdbcTemplate.queryForList("SELECT chat_id FROM chat", Integer.class);
        assertNotNull(count);
        assertFalse(count.isEmpty());
        assertTrue(count.size() == 1);
        assertEquals(1, (int) count.getFirst());
        cnt = jdbcTemplate.update("delete from chat where chat_id = ?", 1l);
        assertTrue(cnt == 1);
    }

    @Test
    @Transactional
    @Rollback
    public void testAddLink() {
        jdbcTemplate.update("insert into chat values (?)", 1l);
        jdbcTemplate.update("insert into link (uri) values (?)", uri);

        List<String> count = jdbcTemplate.queryForList("SELECT uri FROM link", String.class);
        assertNotNull(count);
        assertFalse(count.isEmpty());
        assertTrue(count.size() == 1);
        assertEquals(uri, count.getFirst());
    }
}
