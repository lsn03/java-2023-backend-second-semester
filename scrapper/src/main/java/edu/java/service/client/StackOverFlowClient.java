package edu.java.service.client;

import edu.java.model.stack_over_flow.StackOverFlowModel;
import edu.java.model.stack_over_flow.dto.QuestionAnswerDtoResponse;
import edu.java.model.stack_over_flow.dto.QuestionHeaderDtoResponse;
import java.util.List;

public interface StackOverFlowClient {
    StackOverFlowModel fetchQuestionData(int questionId);

    List<QuestionAnswerDtoResponse> fetchAnswers(int questionId);

    QuestionHeaderDtoResponse fetchHeader(int questionId);
}
