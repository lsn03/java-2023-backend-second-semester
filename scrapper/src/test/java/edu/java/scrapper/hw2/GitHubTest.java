package edu.java.scrapper.hw2;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.model.github.dto.IssueCommentDTO;
import edu.java.model.github.dto.PullCommentDTO;
import edu.java.model.github.dto.PullCommitDTO;
import edu.java.model.github.dto.PullReviewDTO;
import edu.java.model.github.dto.info.UserInfoDTO;
import edu.java.service.GitHubService;
import edu.java.service.client.GitHubClient;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
public class GitHubTest {
    private String owner = "lsn03";
    private String repo = "java";
    private int pullNumber = 1;
    private String baseUrl = "http://localhost:";
    private String expectedUrl = "http://example.com";
    private String login = "lsn03";
    private Long expectedId = 1L;
    private UserInfoDTO expectedUser = new UserInfoDTO(login, expectedId);
    private String body = "body";
    private OffsetDateTime createdAt = OffsetDateTime.of(2024, 2, 20, 19, 3, 10, 0, ZoneOffset.UTC);
    private OffsetDateTime updatedAt = OffsetDateTime.of(2024, 2, 20, 19, 3, 10, 0, ZoneOffset.UTC);
    GitHubClient client;

    @Test
    public void testIssueComments(WireMockRuntimeInfo wireMockRuntimeInfo) {
        int port = wireMockRuntimeInfo.getHttpPort();

        String url = String.format("/repos/%s/%s/issues/%d/comments", owner, repo, pullNumber);

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
        client = new GitHubService(baseUrl + port);
        var response = client.getIssueComments(owner, repo, pullNumber).block();
        assertEquals(expected, response);
    }

    @Test
    public void testPullComments(WireMockRuntimeInfo wireMockRuntimeInfo) {
        int port = wireMockRuntimeInfo.getHttpPort();

        String url = String.format("/repos/%s/%s/pulls/%d/comments", owner, repo, pullNumber);

        PullCommentDTO dto = new PullCommentDTO(expectedId, body, expectedUrl, createdAt, updatedAt, expectedUser);
        List<PullCommentDTO> expected = List.of(dto);
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

        client = new GitHubService(baseUrl);
        client = new GitHubService(baseUrl + port);
        var response = client.getPullComments(owner, repo, pullNumber).block();
        assertEquals(expected, response);
    }

    @Test
    public void testPullReviews(WireMockRuntimeInfo wireMockRuntimeInfo) {
        int port = wireMockRuntimeInfo.getHttpPort();

        String url = String.format("/repos/%s/%s/pulls/%d/reviews", owner, repo, pullNumber);

        PullReviewDTO dto = new PullReviewDTO(expectedId, expectedUrl, expectedUser, body, createdAt);
        List<PullReviewDTO> expected = List.of(dto);
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
                                "submitted_at": "2024-02-20T19:03:10Z",
                                "body": "body"
                                }
                            ]
                            """
                    ).withStatus(200)
                )
        );

        client = new GitHubService(baseUrl + port);
        var response = client.getPullReviews(owner, repo, pullNumber).block();
        assertEquals(expected, response);
    }

    @Test
    public void testPullCommit(WireMockRuntimeInfo wireMockRuntimeInfo) {
        int port = wireMockRuntimeInfo.getHttpPort();

        String url = String.format("/repos/%s/%s/pulls/%d/commits", owner, repo, pullNumber);
        PullCommitDTO.CommitDetail.CommitDetailInfo commitDetailInfo =
            new PullCommitDTO.CommitDetail.CommitDetailInfo(owner, "1@mail.ru", createdAt);
        PullCommitDTO dto = new PullCommitDTO(
            "IC_kwDOKcJnTc5obaHC",
            expectedUrl,
            new PullCommitDTO.CommitDetail(commitDetailInfo, "aboba"),
            expectedUser
        );
        List<PullCommitDTO> expected = List.of(dto);
        WireMock.stubFor(
            WireMock.get(url)
                .willReturn(WireMock.aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(
                        """
                            [
                                {
                                "sha": "IC_kwDOKcJnTc5obaHC",
                                "html_url": "http://example.com",
                                "id": 1,
                                "commit": {
                                    "committer": {
                                       "name": "lsn03",
                                       "email": "1@mail.ru",
                                       "date": "2024-02-20T19:03:10Z"
                                     },
                                    "message": "aboba"
                                },
                                 "committer": {
                                       "login": "lsn03",
                                       "id": 1
                                 }

                                }
                            ]
                            """
                    ).withStatus(200)
                )
        );

        client = new GitHubService(baseUrl + port);
        var response = client.getAllCommitsInPullRequest(owner, repo, pullNumber).block();
        assertEquals(expected, response);
    }

}
