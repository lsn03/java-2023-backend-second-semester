package edu.java.scrapper.hw5;

import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class JdbcChatTest extends IntegrationTest {
    @Autowired
    private JdbcChatRepository jdbcChatRepository;
    @Autowired
    private ApplicationContext context;
    @Test
    public void addTest() {

        jdbcChatRepository.findAll();

    }
}
