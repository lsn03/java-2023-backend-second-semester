package edu.java.bot.service.client;

import edu.java.bot.model.dto.request.AddLinkRequest;
import edu.java.bot.model.dto.request.RemoveLinkRequest;
import edu.java.bot.model.dto.response.ApiErrorResponse;
import edu.java.bot.model.dto.response.LinkResponse;
import edu.java.bot.model.dto.response.ListLinksResponse;
import edu.java.bot.model.dto.response.MyResponse;
import java.util.Optional;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperHttpClient {

    public static final String LINK = "/links";
    public static final String HEADER_TG_CHAT_ID = "Tg-Chat-Id";
    private final WebClient client;
    public static final String TG_CHAT = "/tg-chat/%d";

    public ScrapperHttpClient(String baseUrl) {

        client = WebClient.builder().baseUrl(baseUrl).build();

    }

    public Mono<Optional<ApiErrorResponse>> makeChat(Long id) {

        return client.post()
            .uri(String.format(TG_CHAT, id))
            .exchangeToMono(clientResponse -> {
                if (clientResponse.statusCode().isError()) {
                    return (clientResponse.bodyToMono(ApiErrorResponse.class).map(Optional::of));
                } else {
                    return Mono.just(Optional.empty());
                }
            });

    }

    public Mono<Optional<ApiErrorResponse>> deleteChat(Long id) {

        return client.delete()
            .uri(String.format(TG_CHAT, id))
            .exchangeToMono(clientResponse -> {
                if (clientResponse.statusCode().isError()) {
                    return (clientResponse.bodyToMono(ApiErrorResponse.class).map(Optional::of));
                } else {
                    return Mono.just(Optional.empty());
                }
            });
    }

    public Mono<? extends MyResponse> getLinks(Long id) {
        return client.get()
            .uri(LINK)
            .header(HEADER_TG_CHAT_ID, id.toString())
            .exchangeToMono(clientResponse -> processLink(clientResponse, ListLinksResponse.class));
    }

    public Mono<? extends MyResponse> trackLink(AddLinkRequest addLinkRequest, Long id) {
        return client.post()
            .uri(LINK)
            .header(HEADER_TG_CHAT_ID, id.toString())
            .bodyValue(addLinkRequest)
            .exchangeToMono(clientResponse -> processLink(clientResponse, LinkResponse.class));
    }

    public Mono<? extends MyResponse> unTrackLink(RemoveLinkRequest removeLinkRequest, Long id) {
        return client.method(HttpMethod.DELETE)
            .uri(LINK)
            .header(HEADER_TG_CHAT_ID, id.toString())
            .bodyValue(removeLinkRequest)
            .exchangeToMono(clientResponse -> processLink(clientResponse, LinkResponse.class));
    }

    private static Mono<MyResponse> processLink(
        ClientResponse clientResponse,
        Class<? extends MyResponse> responseClass
    ) {
        if (clientResponse.statusCode().is2xxSuccessful()) {
            return clientResponse.bodyToMono(responseClass);
        } else {
            return clientResponse.bodyToMono(ApiErrorResponse.class);
        }
    }
}
