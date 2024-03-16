package edu.java.service.database.jooq;

import edu.java.domain.model.StackOverFlowAnswerDTO;
import edu.java.domain.repository.jooq.JooqStackOverFlowRepository;
import edu.java.service.database.StackOverFlowService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class JooqStackOverFlowService implements StackOverFlowService {
    private final JooqStackOverFlowRepository jooqStackOverFlowRepository;

    @Override
    public Integer addAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList) {
        return jooqStackOverFlowRepository.addAnswers(stackOverFlowAnswerDTOList);
    }

    @Override
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
