package edu.java.service.database.jooq;

import edu.java.domain.model.StackOverFlowAnswerDto;
import edu.java.domain.repository.jooq.JooqStackOverFlowRepository;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.service.database.StackOverFlowService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JooqStackOverFlowService implements StackOverFlowService {
    private final JooqStackOverFlowRepository jooqStackOverFlowRepository;

    @Override
    @Transactional
    public Integer addAnswers(List<StackOverFlowAnswerDto> stackOverFlowAnswerDtoList) {
        try {
            return jooqStackOverFlowRepository.addAnswers(stackOverFlowAnswerDtoList);
        } catch (DuplicateKeyException e) {
            throw new RecordAlreadyExistException(e);
        }
    }

    @Override
    @Transactional
    public Integer deleteAnswers(List<StackOverFlowAnswerDto> stackOverFlowAnswerDtoList) {
        return jooqStackOverFlowRepository.deleteAnswers(stackOverFlowAnswerDtoList);
    }

    @Override
    public List<StackOverFlowAnswerDto> getAnswers(Long linkId) {
        return jooqStackOverFlowRepository.getAnswers(linkId);
    }

    @Override
    public List<StackOverFlowAnswerDto> getAnswers(URI uri) {
        return jooqStackOverFlowRepository.getAnswers(uri);
    }
}
