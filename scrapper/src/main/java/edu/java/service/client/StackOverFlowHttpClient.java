package edu.java.service.client;

import edu.java.model.stack_over_flow.StackOverFlowModel;
import edu.java.model.stack_over_flow.dto.QuestionAnswerDTOResponse;
import edu.java.model.stack_over_flow.dto.QuestionHeaderDTOResponse;
import edu.java.model.stack_over_flow.wrapper.StackOverFlowAnswerResponseWrapper;
import edu.java.model.stack_over_flow.wrapper.StackOverFlowHeaderResponseWrapper;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverFlowHttpClient implements StackOverFlowClient {
    private static final String STACK_OVER_FLOW_TOKEN = System.getenv().get("APP_SOF_ACCESS_TOKEN");
    private static final String KEY = System.getenv().get("SOF_KEY");
    private final WebClient webClient;

    public StackOverFlowHttpClient(String url) {

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
    public List<QuestionAnswerDTOResponse> fetchAnswers(int questionId) {
        return webClient.get()
            .uri(
                "/questions/{questionId}/answers?site=stackoverflow&access_token={token}&key={key}",
                questionId,
                STACK_OVER_FLOW_TOKEN,
                KEY
            )
            .retrieve()
            .bodyToMono(StackOverFlowAnswerResponseWrapper.class)
            .map(StackOverFlowAnswerResponseWrapper::getQuestionAnswerDTOList)
            .block();
    }

    @Override
    public QuestionHeaderDTOResponse fetchHeader(int questionId) {
        return webClient.get()
            .uri(
                "/questions/{questionId}?order=desc&sort=activity&site=stackoverflow&access_token={token}&key={key}",
                questionId,
                STACK_OVER_FLOW_TOKEN,
                KEY
            )
            .retrieve()
            .bodyToMono(StackOverFlowHeaderResponseWrapper.class)
            .map(stackOverFlowHeaderResponseWrapper -> stackOverFlowHeaderResponseWrapper.getList().get(0))
            .block();
    }
}
