package edu.java.service.client;

import edu.java.configuration.ApplicationConfig;
import edu.java.model.stack_over_flow.StackOverFlowModel;
import edu.java.model.stack_over_flow.dto.QuestionAnswerDtoResponse;
import edu.java.model.stack_over_flow.dto.QuestionHeaderDtoResponse;
import edu.java.model.stack_over_flow.wrapper.StackOverFlowAnswerResponseWrapper;
import edu.java.model.stack_over_flow.wrapper.StackOverFlowHeaderResponseWrapper;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverFlowHttpClient implements StackOverFlowClient {
    private final String stackOverFlowToken;
    private final String key;
    private final WebClient webClient;

    public StackOverFlowHttpClient(String url, ApplicationConfig config) {
        this.key = config.stackOverFlowApiProperties().key();
        this.stackOverFlowToken = config.stackOverFlowApiProperties().token();
        webClient = WebClient.builder()
            .baseUrl(url)
            .defaultHeader("User-Agent", "lsn03SOF")
            .defaultHeader("Accept", "application/json")
            .build();
    }

    @Override
    public StackOverFlowModel fetchQuestionData(int questionId) {
        var ans = fetchAnswers(questionId);
        var header = fetchHeader(questionId);

        return new StackOverFlowModel(header, ans);

    }

    @Override
    public List<QuestionAnswerDtoResponse> fetchAnswers(int questionId) {
        return webClient.get()
            .uri(
                "/questions/{questionId}/answers?site=stackoverflow&access_token={token}&key={key}",
                questionId,
                stackOverFlowToken,
                key
            )
            .retrieve()
            .bodyToMono(StackOverFlowAnswerResponseWrapper.class)
            .map(StackOverFlowAnswerResponseWrapper::getQuestionAnswerDTOList)
            .block();
    }

    @Override
    public QuestionHeaderDtoResponse fetchHeader(int questionId) {
        return webClient.get()
            .uri(
                "/questions/{questionId}?order=desc&sort=activity&site=stackoverflow&access_token={token}&key={key}",
                questionId,
                stackOverFlowToken,
                key
            )
            .retrieve()
            .bodyToMono(StackOverFlowHeaderResponseWrapper.class)
            .map(stackOverFlowHeaderResponseWrapper -> stackOverFlowHeaderResponseWrapper.getList().get(0))
            .block();
    }
}
