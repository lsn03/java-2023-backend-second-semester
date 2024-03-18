package edu.java.service.database.jdbc;

import edu.java.domain.model.StackOverFlowAnswerDTO;
import edu.java.domain.repository.jdbc.JdbcStackOverFlowRepository;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.service.database.StackOverFlowService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcStackOverFlowService implements StackOverFlowService {
    private final JdbcStackOverFlowRepository jdbcStackOverFlowRepository;

    @Override
    @Transactional
    public Integer addAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList) {
        try {
            return jdbcStackOverFlowRepository.addAnswers(stackOverFlowAnswerDTOList);
        } catch (DuplicateKeyException e) {
            throw new RecordAlreadyExistException(e);
        }
    }

    @Override
    @Transactional
    public Integer deleteAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList) {
        return jdbcStackOverFlowRepository.deleteAnswers(stackOverFlowAnswerDTOList);
    }

    @Override
    @Transactional
    public List<StackOverFlowAnswerDTO> getAnswers(Long linkId) {
        return jdbcStackOverFlowRepository.getAnswers(linkId);
    }

    @Override
    @Transactional
    public List<StackOverFlowAnswerDTO> getAnswers(URI uri) {
        return jdbcStackOverFlowRepository.getAnswers(uri);
    }
}
