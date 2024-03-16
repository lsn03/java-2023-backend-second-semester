package edu.java.domain.repository;

import edu.java.domain.model.StackOverFlowAnswerDTO;
import java.net.URI;
import java.util.List;

public interface StackOverFlowRepository {
    Integer addAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList);

    Integer deleteAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList);

    List<StackOverFlowAnswerDTO> getAnswers(Long linkId);

    List<StackOverFlowAnswerDTO> getAnswers(URI uri);

}
