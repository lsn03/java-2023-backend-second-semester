package edu.java.service.client;

import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.model.scrapper.dto.response.ApiErrorResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotHttpClient {
    private final WebClient client;

    public BotHttpClient(String baseUrl) {
        client = WebClient.builder().baseUrl(baseUrl).build();
    }

    public Mono<ApiErrorResponse> sendUpdates(LinkUpdateRequest linkUpdateRequest) {
        return client.post().uri("/updates").bodyValue(linkUpdateRequest).exchangeToMono(clientResponse -> {
            if (clientResponse.statusCode().isError()) {
                return (clientResponse.bodyToMono(ApiErrorResponse.class));
            } else {
                return Mono.empty();
            }
        });
    }
}
