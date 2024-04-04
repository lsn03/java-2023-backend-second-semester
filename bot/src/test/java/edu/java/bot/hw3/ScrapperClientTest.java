package edu.java.bot.hw3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.RetryConfig;
import edu.java.bot.configuration.RetryType;
import edu.java.bot.exception.ApiErrorException;
import edu.java.bot.model.dto.request.AddLinkRequest;
import edu.java.bot.model.dto.request.RemoveLinkRequest;
import edu.java.bot.model.dto.response.ApiErrorResponse;
import edu.java.bot.model.dto.response.LinkResponse;
import edu.java.bot.model.dto.response.ListLinksResponse;
import edu.java.bot.service.client.ScrapperHttpClient;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@WireMockTest
public class ScrapperClientTest {
    private static final String TG_CHAT_TEMPLATE = "/tg-chat/%d";
    private static final String LINKS = "/links";
    private static final String HEADER_TG_CHAT_ID = "Tg-Chat-Id";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private final String baseUrl = "http://localhost:";
    private final RetryConfig retryConfig =
        new RetryConfig(RetryType.CONSTANT, 3L, Duration.ofSeconds(5), Duration.ofSeconds(5), Set.of());

    ApplicationConfig.ScrapperProperties properties;
    ScrapperHttpClient client;

    ApiErrorResponse apiErrorResponse;
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String json;
    long chatId = 1;

    @Test
    public void createChatSuccess(WireMockRuntimeInfo wireMockRuntimeInfo) {
        int port = wireMockRuntimeInfo.getHttpPort();

        String baseUrlWithPort = baseUrl + port;
        properties = new ApplicationConfig.ScrapperProperties(baseUrlWithPort, retryConfig);
        client = new ScrapperHttpClient(properties);

        String url = String.format(TG_CHAT_TEMPLATE, chatId);

        WireMock.stubFor(
            WireMock.post(url)
                .willReturn(WireMock.aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .withStatus(200)
                )
        );
        assertDoesNotThrow(() -> client.makeChat(chatId));

    }

    @Test
    public void createChatError(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        properties = new ApplicationConfig.ScrapperProperties(baseUrlWithPort, retryConfig);
        client = new ScrapperHttpClient(properties);

        String url = String.format(TG_CHAT_TEMPLATE, chatId);

        apiErrorResponse = new ApiErrorResponse(
            "User Already Exist",
            "409",
            "UserExistException",
            "This user already Exist",
            List.of()
        );

        json = ow.writeValueAsString(apiErrorResponse);

        WireMock.stubFor(
            WireMock.post(url)
                .willReturn(WireMock.aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .withBody(json)
                    .withStatus(409)
                )
        );

        try {
            client.makeChat(chatId);
        } catch (ApiErrorException e) {
            assertEquals(apiErrorResponse, e.getErrorResponse());
        }

    }

    @Test
    public void deleteChatSuccess(WireMockRuntimeInfo wireMockRuntimeInfo) {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        properties = new ApplicationConfig.ScrapperProperties(baseUrlWithPort, retryConfig);
        client = new ScrapperHttpClient(properties);

        String url = String.format(TG_CHAT_TEMPLATE, chatId);

        WireMock.stubFor(
            WireMock.delete(url)
                .willReturn(WireMock.aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .withStatus(200)
                )
        );
        assertNull(client.deleteChat(chatId));
    }

    @Test
    public void deleteChatError(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        properties = new ApplicationConfig.ScrapperProperties(baseUrlWithPort, retryConfig);
        client = new ScrapperHttpClient(properties);

        String url = String.format(TG_CHAT_TEMPLATE, chatId);

        apiErrorResponse =
            new ApiErrorResponse("User Does not Exist", "404", "UserDoesNotExistException", "", List.of());

        json = ow.writeValueAsString(apiErrorResponse);

        WireMock.stubFor(
            WireMock.delete(url)
                .willReturn(WireMock.aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .withBody(json)
                    .withStatus(404)
                )
        );

        try {
            client.deleteChat(chatId);
        } catch (ApiErrorException e) {
            assertEquals(apiErrorResponse, e.getErrorResponse());
        }

    }

    @Test
    public void trackLinkSuccess(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        properties = new ApplicationConfig.ScrapperProperties(baseUrlWithPort, retryConfig);
        client = new ScrapperHttpClient(properties);

        AddLinkRequest req = new AddLinkRequest("http:/example.com");
        LinkResponse linkResponse = new LinkResponse(1L, URI.create("http:/example.com"));
        json = ow.writeValueAsString(linkResponse);
        WireMock.stubFor(
            WireMock.post(LINKS)
                .willReturn(WireMock.aResponse()
                    .withHeader(HEADER_TG_CHAT_ID, String.valueOf(chatId))
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .withBody(json)
                    .withStatus(200)
                )
        );
        var response = (client.trackLink(req, chatId));
        assertNotNull(response);
        assertEquals(linkResponse, response);
    }

    @Test
    public void trackLinkError(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        properties = new ApplicationConfig.ScrapperProperties(baseUrlWithPort, retryConfig);
        client = new ScrapperHttpClient(properties);

        AddLinkRequest req = new AddLinkRequest("http:/example.com");
        apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setCode("1");
        json = ow.writeValueAsString(apiErrorResponse);
        WireMock.stubFor(
            WireMock.post(LINKS)
                .willReturn(WireMock.aResponse()
                    .withHeader(HEADER_TG_CHAT_ID, String.valueOf(chatId))
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .withBody(json)
                    .withStatus(400)
                )
        );

        try {
            client.trackLink(req, chatId);
        } catch (ApiErrorException e) {
            assertEquals(apiErrorResponse, e.getErrorResponse());
        }
//        assertTrue(response instanceof ApiErrorResponse);

    }

    @Test
    public void getLinkListSuccess(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        properties = new ApplicationConfig.ScrapperProperties(baseUrlWithPort, retryConfig);
        client = new ScrapperHttpClient(properties);

        ListLinksResponse req = new ListLinksResponse(List.of(), 0);
        json = ow.writeValueAsString(req);
        WireMock.stubFor(
            WireMock.get(LINKS)
                .willReturn(WireMock.aResponse()
                    .withHeader(HEADER_TG_CHAT_ID, String.valueOf(chatId))
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .withBody(json)
                    .withStatus(200)
                )
        );
        var response = (client.getLinks(chatId));
        assertEquals(req, response);
    }

    @Test
    public void getLinkListError(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        properties = new ApplicationConfig.ScrapperProperties(baseUrlWithPort, retryConfig);
        client = new ScrapperHttpClient(properties);

        apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setCode("1");
        json = ow.writeValueAsString(apiErrorResponse);
        WireMock.stubFor(
            WireMock.get(LINKS)
                .willReturn(WireMock.aResponse()
                    .withHeader(HEADER_TG_CHAT_ID, String.valueOf(chatId))
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .withBody(json)
                    .withStatus(400)
                )
        );
        try {
            client.getLinks(chatId);
        } catch (ApiErrorException e) {
            assertEquals(apiErrorResponse, e.getErrorResponse());
        }

    }

    @Test
    public void unTrackLinkSuccess(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        properties = new ApplicationConfig.ScrapperProperties(baseUrlWithPort, retryConfig);
        client = new ScrapperHttpClient(properties);

        RemoveLinkRequest req = new RemoveLinkRequest("http:/example.com");
        LinkResponse linkResponse = new LinkResponse(1L, URI.create("http:/example.com"));
        json = ow.writeValueAsString(linkResponse);
        WireMock.stubFor(
            WireMock.delete(LINKS)
                .willReturn(WireMock.aResponse()
                    .withHeader(HEADER_TG_CHAT_ID, String.valueOf(chatId))
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .withBody(json)
                    .withStatus(200)
                )
        );
        var response = (client.unTrackLink(req, chatId));
        assertNotNull(response);
        assertEquals(linkResponse, response);
    }

    @Test
    public void unTrackLinkError(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        properties = new ApplicationConfig.ScrapperProperties(baseUrlWithPort, retryConfig);
        client = new ScrapperHttpClient(properties);

        RemoveLinkRequest req = new RemoveLinkRequest("http:/example.com");
        apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setCode("1");
        json = ow.writeValueAsString(apiErrorResponse);
        WireMock.stubFor(
            WireMock.delete(LINKS)
                .willReturn(WireMock.aResponse()
                    .withHeader(HEADER_TG_CHAT_ID, String.valueOf(chatId))
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .withBody(json)
                    .withStatus(400)
                )
        );
        try {
            client.unTrackLink(req, chatId);
        } catch (ApiErrorException e) {
            assertEquals(apiErrorResponse, e.getErrorResponse());
        }

    }

    @Test
    public void tooManyRequest(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        properties = new ApplicationConfig.ScrapperProperties(baseUrlWithPort, retryConfig);
        client = new ScrapperHttpClient(properties);

        apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setCode("429");
        json = ow.writeValueAsString(apiErrorResponse);
        WireMock.stubFor(
            WireMock.get(LINKS)
                .willReturn(WireMock.aResponse()
                    .withHeader(HEADER_TG_CHAT_ID, String.valueOf(chatId))
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .withBody(json)
                    .withStatus(429)
                )
        );

        try {
            client.getLinks(chatId);
        } catch (ApiErrorException e) {
            assertEquals(apiErrorResponse, e.getErrorResponse());
        }

    }

}
