package edu.java.service.client;

import edu.java.model.stack_over_flow.StackOverFlowModel;
import edu.java.model.stack_over_flow.dto.QuestionAnswerDTO;
import edu.java.model.stack_over_flow.dto.QuestionHeaderDTO;
import java.util.List;
import reactor.core.publisher.Mono;

public interface StackOverFlowClient {
    Mono<StackOverFlowModel> fetchQuestionData(int questionId);

    Mono<List<QuestionAnswerDTO>> fetchAnswers(int questionId);

    Mono<QuestionHeaderDTO> fetchHeader(int questionId);
}
