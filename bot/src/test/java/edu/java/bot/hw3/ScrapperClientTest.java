package edu.java.bot.hw3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.model.dto.response.ApiErrorResponse;
import edu.java.bot.service.client.ScrapperHttpClient;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@WireMockTest
public class ScrapperClientTest {
    public static final String TG_CHAT_TEMPLATE = "/tg-chat/%d";
    public static final String LINKS = "/links";
    private String baseUrl = "http://localhost:";
    ScrapperHttpClient client;

    ApiErrorResponse apiErrorResponse;
    ObjectWriter om = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String json;
    long chatId = 1;

    @Test
    public void createChatSuccess(WireMockRuntimeInfo wireMockRuntimeInfo) {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        client = new ScrapperHttpClient(baseUrlWithPort);

        String url = String.format(TG_CHAT_TEMPLATE, chatId);

        WireMock.stubFor(
            WireMock.post(url)
                .willReturn(WireMock.aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                )
        );
        assertNull(client.makeChat(chatId).block());
    }

    @Test
    public void createChatError(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        client = new ScrapperHttpClient(baseUrlWithPort);

        String url = String.format(TG_CHAT_TEMPLATE, chatId);

        apiErrorResponse = new ApiErrorResponse("User Already Exist",
            "409",
            "UserExistException",
            "This user already Exist",
            List.of()
        );

        json = om.writeValueAsString(apiErrorResponse);

        WireMock.stubFor(
            WireMock.post(url)
                .willReturn(WireMock.aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(json)
                    .withStatus(409)
                )
        );

        ApiErrorResponse response = (client.makeChat(chatId).block());
        assertEquals(apiErrorResponse, response);
    }

    @Test
    public void deleteChatSuccess(WireMockRuntimeInfo wireMockRuntimeInfo) {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        client = new ScrapperHttpClient(baseUrlWithPort);

        String url = String.format(TG_CHAT_TEMPLATE, chatId);

        WireMock.stubFor(
            WireMock.delete(url)
                .willReturn(WireMock.aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                )
        );
        assertNull(client.deleteChat(chatId).block());
    }

    @Test
    public void deleteChatError(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        client = new ScrapperHttpClient(baseUrlWithPort);

        String url = String.format(TG_CHAT_TEMPLATE, chatId);

        apiErrorResponse =
            new ApiErrorResponse("User Does not Exist", "404", "UserDoesNotExistException", "", List.of());

        json = om.writeValueAsString(apiErrorResponse);

        WireMock.stubFor(
            WireMock.delete(url)
                .willReturn(WireMock.aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(json)
                    .withStatus(404)
                )
        );

        ApiErrorResponse response = (client.deleteChat(chatId).block());
        assertEquals(apiErrorResponse, response);
    }

//    @Test
//    public void getLinkSuccess(WireMockRuntimeInfo wireMockRuntimeInfo)  {
//        int port = wireMockRuntimeInfo.getHttpPort();
//        String baseUrlWithPort = baseUrl + port;
//        client = new ScrapperHttpClient(baseUrlWithPort);
//
//
//
//        WireMock.stubFor(
//            WireMock.get(LINKS)
//                .willReturn(WireMock.aResponse()
//                    .withHeader("Content-Type", "application/json")
//                    .withStatus(200)
//                )
//        );
////        assertNull(client.trackLink(chatId).block());
//    }
}
