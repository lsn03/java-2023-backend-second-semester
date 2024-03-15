package edu.java.service.client;

import edu.java.model.stack_over_flow.StackOverFlowModel;
import edu.java.model.stack_over_flow.dto.QuestionAnswerDTOResponse;
import edu.java.model.stack_over_flow.dto.QuestionHeaderDTOResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface StackOverFlowClient {
    StackOverFlowModel fetchQuestionData(int questionId);

    List<QuestionAnswerDTOResponse> fetchAnswers(int questionId);

    QuestionHeaderDTOResponse fetchHeader(int questionId);
}
