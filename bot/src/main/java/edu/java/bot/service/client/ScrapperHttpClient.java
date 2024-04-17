package edu.java.bot.service.client;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.exception.ApiErrorException;
import edu.java.bot.model.dto.request.AddLinkRequest;
import edu.java.bot.model.dto.request.RemoveLinkRequest;
import edu.java.bot.model.dto.response.ApiErrorResponse;
import edu.java.bot.model.dto.response.LinkResponse;
import edu.java.bot.model.dto.response.ListLinksResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class ScrapperHttpClient {

    private static final String LINK = "/links";
    private static final String HEADER_TG_CHAT_ID = "Tg-Chat-Id";
    private static final String TG_CHAT = "/tg-chat/%d";

    private final WebClient client;
    private final Retry retry;

    public ScrapperHttpClient(ApplicationConfig config) {
        client = WebClient.builder().baseUrl(config.scrapperBaseUrl()).build();
        this.retry = config.retry().getRetrySpec();
    }

    public ApiErrorResponse makeChat(Long id) throws ApiErrorException {

        return client.post()
            .uri(String.format(TG_CHAT, id))
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                clientResponse -> (clientResponse.bodyToMono(ApiErrorResponse.class).map(ApiErrorException::new)
                    .flatMap(Mono::error))
            )
            .bodyToMono(ApiErrorResponse.class)
            .retryWhen(retry)
            .block();

    }

    public ApiErrorResponse deleteChat(Long id) throws ApiErrorException {

        return client.delete()
            .uri(String.format(TG_CHAT, id))
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                clientResponse -> (clientResponse.bodyToMono(ApiErrorResponse.class).map(ApiErrorException::new)
                    .flatMap(Mono::error))
            )
            .bodyToMono(ApiErrorResponse.class)
            .retryWhen(retry)
            .block();
    }

    public ListLinksResponse getLinks(Long id) throws ApiErrorException {
        return client.get()
            .uri(LINK)
            .header(HEADER_TG_CHAT_ID, id.toString())
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                clientResponse -> (clientResponse.bodyToMono(ApiErrorResponse.class).map(ApiErrorException::new)
                    .flatMap(Mono::error))
            )
            .bodyToMono(ListLinksResponse.class)
            .retryWhen(retry)
            .block();

    }

    public LinkResponse trackLink(AddLinkRequest addLinkRequest, Long id) throws ApiErrorException {
        return client.post()
            .uri(LINK)
            .header(HEADER_TG_CHAT_ID, id.toString())
            .bodyValue(addLinkRequest)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                clientResponse -> (clientResponse.bodyToMono(ApiErrorResponse.class).map(ApiErrorException::new)
                    .flatMap(Mono::error))
            )
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry)
            .block();

    }

    public LinkResponse unTrackLink(RemoveLinkRequest removeLinkRequest, Long id) throws ApiErrorException {
        return client.method(HttpMethod.DELETE)
            .uri(LINK)
            .header(HEADER_TG_CHAT_ID, id.toString())
            .bodyValue(removeLinkRequest)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                clientResponse -> (clientResponse.bodyToMono(ApiErrorResponse.class).map(ApiErrorException::new)
                    .flatMap(Mono::error))
            )
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry)
            .block();
    }

}
