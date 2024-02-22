package edu.java.service;

import edu.java.model.stack_over_flow.StackOverFlowModel;
import edu.java.model.stack_over_flow.dto.QuestionAnswerDTOResponse;
import edu.java.model.stack_over_flow.dto.QuestionHeaderDTOResponse;
import edu.java.model.stack_over_flow.wrapper.StackOverFlowAnswerResponseWrapper;
import edu.java.model.stack_over_flow.wrapper.StackOverFlowHeaderResponseWrapper;
import edu.java.service.client.StackOverFlowClient;
import edu.java.util.BaseUrl;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverFlowService implements StackOverFlowClient {
    private static final String STACK_OVER_FLOW_TOKEN = System.getenv().get("APP_SOF_ACCESS_TOKEN");

    private final WebClient webClient;

    public StackOverFlowService() {
        this(BaseUrl.STACK_OVER_FLOW_BASE_URL.getUrl());
    }

    public StackOverFlowService(String url) {
        String stackOverFlowBaseApi = BaseUrl.STACK_OVER_FLOW_BASE_URL.getUrl();

        if (!url.isEmpty() && !url.isBlank()) {
            stackOverFlowBaseApi = url;
        }
        webClient = WebClient.builder()
            .baseUrl(stackOverFlowBaseApi)
            .defaultHeader("User-Agent", "lsn03")
            .defaultHeader("Accept", "application/json")
            .build();
    }

    @Override
    public Mono<StackOverFlowModel> fetchQuestionData(int questionId) {
        var ans = fetchAnswers(questionId);
        var header = fetchHeader(questionId);

        return Mono.zip(ans, header).map(objects -> {
            StackOverFlowModel model = new StackOverFlowModel();
            model.setQuestionHeader(objects.getT2());
            model.setQuestionAnswerList(objects.getT1());
            return model;
        });
    }

    @Override
    public Mono<List<QuestionAnswerDTOResponse>> fetchAnswers(int questionId) {
        return webClient.get()
            .uri("/questions/{questionId}/answers?site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(StackOverFlowAnswerResponseWrapper.class)
            .map(StackOverFlowAnswerResponseWrapper::getQuestionAnswerDTOList);

    }

    @Override
    public Mono<QuestionHeaderDTOResponse> fetchHeader(int questionId) {
        return webClient.get()
            .uri("/questions/{questionId}?order=desc&sort=activity&site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(StackOverFlowHeaderResponseWrapper.class)
            .map(stackOverFlowHeaderResponseWrapper -> stackOverFlowHeaderResponseWrapper.getList().get(0));
    }
}
