package edu.java.scrapper.hw5bonus.jooq;

import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JooqChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqChatRepository jooqChatRepository;

    @Test
    @Transactional
    @Rollback
    public void test1(){
        System.out.println(POSTGRES.getJdbcUrl());
        jooqChatRepository.add(1l);
        var response = (jooqChatRepository.findAll());
        System.out.println(response);
    }
}
