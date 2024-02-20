package edu.java.scrapper.hw2;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.model.github.dto.IssueCommentDTO;
import edu.java.model.github.dto.info.UserInfoDTO;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import edu.java.service.GitHubService;
import edu.java.service.client.BaseUrl;
import edu.java.service.client.GitHubClient;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
public class GitHubTest {

    @Test
    public void test1(WireMockRuntimeInfo wmRuntimeInfo) {
        String owner = "lsn03";
        String repo = "java";
        int pullNumber = 1;
        String url = String.format("/repos/%s/%s/issues/%d/comments", owner, repo, pullNumber);

        String baseUrl = "http://localhost:8080";
        String expectedUrl = "http://example.com";
        String login = "lsn03";
        Long expectedId = 1L;
        UserInfoDTO expectedUser = new UserInfoDTO(login, expectedId);
        String body = "body";
        OffsetDateTime createdAt = OffsetDateTime.of(2024, 2, 20, 19, 3, 10, 0, ZoneOffset.UTC);
        OffsetDateTime updatedAt = OffsetDateTime.of(2024, 2, 20, 19, 3, 10, 0, ZoneOffset.UTC);
        

        IssueCommentDTO dto = new IssueCommentDTO(expectedUrl, expectedId, expectedUser, body, createdAt, updatedAt);
        List<IssueCommentDTO> expected = List.of(dto);
        WireMock.stubFor(
            WireMock.get(url)
                .willReturn(WireMock.aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(
                        """
                            [
                                {
                                "node_id": "IC_kwDOKcJnTc5obaHC",
                                "html_url": "http://example.com",
                                "id": 1,
                                "user": {
                                    "login": "lsn03",
                                    "id": 1
                                },
                                "created_at": "2024-02-20T19:03:10Z",
                                "updated_at": "2024-02-20T19:03:10Z",
                                "body": "body"
                                }
                            ]
                            """
                    ).withStatus(200)
            )
        );

        GitHubClient client = new GitHubService(baseUrl);
        var response = client.getIssueComments(owner, repo, pullNumber).block();
        assertEquals(expected,response);
    }
}
