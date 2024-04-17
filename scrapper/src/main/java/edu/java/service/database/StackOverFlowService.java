package edu.java.service.database;

import edu.java.domain.model.StackOverFlowAnswerDto;
import java.net.URI;
import java.util.List;

public interface StackOverFlowService {
    Integer addAnswers(List<StackOverFlowAnswerDto> stackOverFlowAnswerDtoList);

    Integer deleteAnswers(List<StackOverFlowAnswerDto> stackOverFlowAnswerDtoList);

    List<StackOverFlowAnswerDto> getAnswers(Long linkId);

    List<StackOverFlowAnswerDto> getAnswers(URI uri);
}
