package edu.java.service.client;

import edu.java.model.stack_over_flow.StackOverFlowModel;
import edu.java.model.stack_over_flow.dto.QuestionAnswerDtoResponse;
import edu.java.model.stack_over_flow.dto.QuestionHeaderDtoResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface StackOverFlowClient {
    Mono<StackOverFlowModel> fetchQuestionData(int questionId);

    Mono<List<QuestionAnswerDtoResponse>> fetchAnswers(int questionId);

    Mono<QuestionHeaderDtoResponse> fetchHeader(int questionId);
}
