package edu.java.service.database.jpa;

import edu.java.domain.model.StackOverFlowAnswerDto;
import edu.java.domain.repository.jpa.JpaStackOverFlowRepository;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.service.database.StackOverFlowService;
import jakarta.persistence.EntityExistsException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor

public class JpaStackOverFlowService implements StackOverFlowService {
    private final JpaStackOverFlowRepository stackOverFlowRepository;

    @Override
    @Transactional
    public Integer addAnswers(List<StackOverFlowAnswerDto> stackOverFlowAnswerDTOList) {
        try {
            return stackOverFlowRepository.addAnswers(stackOverFlowAnswerDTOList);
        } catch (EntityExistsException | ConstraintViolationException e) {
            throw new RecordAlreadyExistException(e);
        }
    }

    @Override
    @Transactional
    public Integer deleteAnswers(List<StackOverFlowAnswerDto> stackOverFlowAnswerDTOList) {

        return stackOverFlowRepository.deleteAnswers(stackOverFlowAnswerDTOList);

    }

    @Override
    public List<StackOverFlowAnswerDto> getAnswers(Long linkId) {
        return stackOverFlowRepository.getAnswers(linkId);
    }

    @Override
    public List<StackOverFlowAnswerDto> getAnswers(URI uri) {
        return stackOverFlowRepository.getAnswers(uri);
    }
}
