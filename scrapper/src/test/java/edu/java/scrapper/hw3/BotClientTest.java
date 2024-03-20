package edu.java.scrapper.hw3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.model.scrapper.dto.response.ApiErrorResponse;
import edu.java.service.client.BotHttpClient;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@WireMockTest
public class BotClientTest {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    private final String baseUrl = "http://localhost:";
    private BotHttpClient client;
    private final LinkUpdateRequest linkUpdateRequest =
        new LinkUpdateRequest(1l, "http://example.com", "decr", List.of(1l, 2l, 3l));
    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    private String json;
    private final String url = "/updates";

    @Test
    public void testSuccess(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        client = new BotHttpClient(baseUrlWithPort);

        json = ow.writeValueAsString(linkUpdateRequest);
        WireMock.stubFor(
            WireMock.post(url)
                .willReturn(WireMock.aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .withBody(json)
                    .withStatus(200)
                )
        );
        assertNull(client.sendUpdates(linkUpdateRequest).block());
    }

    @Test
    public void testError(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        int port = wireMockRuntimeInfo.getHttpPort();
        String baseUrlWithPort = baseUrl + port;
        client = new BotHttpClient(baseUrlWithPort);

        ApiErrorResponse response =
            new ApiErrorResponse("Exception", "400", "IncorrectParametersException", "Missing field", List.of());

        json = ow.writeValueAsString(response);
        WireMock.stubFor(
            WireMock.post(url)
                .willReturn(WireMock.aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .withBody(json)
                    .withStatus(400)
                )
        );
        ApiErrorResponse updateResponse = client.sendUpdates(linkUpdateRequest).block();

        assertEquals(response, updateResponse);
    }
}
