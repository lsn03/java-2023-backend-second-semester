package edu.java.scrapper.hw3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.model.scrapper.dto.response.ApiErrorResponse;
import edu.java.service.BotHttpClient;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@WireMockTest
public class BotClientTest {
    private String baseUrl = "http://localhost:";
    BotHttpClient client;
    LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(1l, "http://example.com", "decr", List.of(1, 2, 3));
    ApiErrorResponse response =
        new ApiErrorResponse("Exception", "400", "IncorrectParametersException", "Missing field", List.of());
    ObjectWriter om = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String json;

    @Test
    public void test1(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + String.valueOf(port);
        client = new BotHttpClient(baseUrlWithPort);
        String url = "/updates";

        json = om.writeValueAsString(linkUpdateRequest);
        WireMock.stubFor(
            WireMock.post(url)
                .willReturn(WireMock.aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(json).withStatus(200)
                )
        );
        assertNull(client.sendUpdates(linkUpdateRequest).block());
    }

    @Test
    public void test2(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + String.valueOf(port);
        client = new BotHttpClient(baseUrlWithPort);
        String url = "/updates";

        String json = om.writeValueAsString(response);
        WireMock.stubFor(
            WireMock.post(url)
                .willReturn(WireMock.aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(json).withStatus(400)
                )
        );
        ApiErrorResponse updateResponse = client.sendUpdates(linkUpdateRequest).block();

        assertEquals(response,updateResponse);
    }
}
