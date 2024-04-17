package edu.java.service.database.jdbc;

import edu.java.domain.model.StackOverFlowAnswerDto;
import edu.java.domain.repository.jdbc.JdbcStackOverFlowRepository;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.service.database.StackOverFlowService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcStackOverFlowService implements StackOverFlowService {
    private final JdbcStackOverFlowRepository jdbcStackOverFlowRepository;

    @Override
    @Transactional
    public Integer addAnswers(List<StackOverFlowAnswerDto> stackOverFlowAnswerDtoList) {
        try {
            return jdbcStackOverFlowRepository.addAnswers(stackOverFlowAnswerDtoList);
        } catch (DuplicateKeyException e) {
            throw new RecordAlreadyExistException(e);
        }
    }

    @Override
    @Transactional
    public Integer deleteAnswers(List<StackOverFlowAnswerDto> stackOverFlowAnswerDtoList) {
        return jdbcStackOverFlowRepository.deleteAnswers(stackOverFlowAnswerDtoList);
    }

    @Override
    public List<StackOverFlowAnswerDto> getAnswers(Long linkId) {
        return jdbcStackOverFlowRepository.getAnswers(linkId);
    }

    @Override
    public List<StackOverFlowAnswerDto> getAnswers(URI uri) {
        return jdbcStackOverFlowRepository.getAnswers(uri);
    }
}
