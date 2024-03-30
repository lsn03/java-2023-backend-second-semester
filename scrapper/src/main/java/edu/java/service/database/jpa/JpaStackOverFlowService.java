package edu.java.service.database.jpa;

import edu.java.domain.model.StackOverFlowAnswerDTO;
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
    public Integer addAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList) {
        try {
            return stackOverFlowRepository.addAnswers(stackOverFlowAnswerDTOList);
        } catch (EntityExistsException | ConstraintViolationException e) {
            throw new RecordAlreadyExistException(e);
        }
    }

    @Override
    @Transactional
    public Integer deleteAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList) {

        return stackOverFlowRepository.deleteAnswers(stackOverFlowAnswerDTOList);

    }

    @Override
    public List<StackOverFlowAnswerDTO> getAnswers(Long linkId) {
        return stackOverFlowRepository.getAnswers(linkId);
    }

    @Override
    public List<StackOverFlowAnswerDTO> getAnswers(URI uri) {
        return stackOverFlowRepository.getAnswers(uri);
    }
}
