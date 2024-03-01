package edu.java.scrapper.hw4;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MigrationTest extends IntegrationTest {

    private JdbcTemplate jdbcTemplate;

    @Test
    public void testMigration() {
        jdbcTemplate = new JdbcTemplate(
            new DriverManagerDataSource(
                POSTGRES.getJdbcUrl(),
                POSTGRES.getUsername(),
                POSTGRES.getPassword()
            )
        );
        List<Integer> count = jdbcTemplate.queryForList("SELECT chat_id FROM chat", Integer.class);
        assertNotNull(count);
        assertFalse(count.isEmpty());

    }
}
