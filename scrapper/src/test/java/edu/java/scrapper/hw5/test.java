package edu.java.scrapper.hw5;

import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class test  extends IntegrationTest {

    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    @Autowired
    private JdbcTemplate template;
    @Test
    public void test2(){
    template.execute("");
        jdbcChatRepository.findAll();
    }
}
