package edu.java.service.database.jooq;

import edu.java.domain.model.StackOverFlowAnswerDTO;
import edu.java.domain.repository.jooq.JooqStackOverFlowRepository;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.service.database.StackOverFlowService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Primary
public class JooqStackOverFlowService implements StackOverFlowService {
    private final JooqStackOverFlowRepository jooqStackOverFlowRepository;

    @Override
    @Transactional
    public Integer addAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList) {
        try {
            return jooqStackOverFlowRepository.addAnswers(stackOverFlowAnswerDTOList);
        } catch (DuplicateKeyException e) {
            throw new RecordAlreadyExistException(e);
        }
    }

    @Override
    @Transactional
    public Integer deleteAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList) {
        return jooqStackOverFlowRepository.deleteAnswers(stackOverFlowAnswerDTOList);
    }

    @Override
    public List<StackOverFlowAnswerDTO> getAnswers(Long linkId) {
        return jooqStackOverFlowRepository.getAnswers(linkId);
    }

    @Override
    public List<StackOverFlowAnswerDTO> getAnswers(URI uri) {
        return jooqStackOverFlowRepository.getAnswers(uri);
    }
}
