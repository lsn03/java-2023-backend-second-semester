package edu.java.service.database.jpa;

import edu.java.domain.model.StackOverFlowAnswerDTO;
import edu.java.domain.repository.jpa.JpaStackOverFlowRepository;
import edu.java.exception.exception.RecordAlreadyExistException;
import edu.java.service.database.StackOverFlowService;
import jakarta.persistence.EntityExistsException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class JpaStackOverFlowService implements StackOverFlowService {
    private final JpaStackOverFlowRepository stackOverFlowRepository;

    @Override
    public Integer addAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList) {
        try {
            return stackOverFlowRepository.addAnswers(stackOverFlowAnswerDTOList);
        } catch (EntityExistsException e) {
            throw new RecordAlreadyExistException(e);
        }
    }

    @Override
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
