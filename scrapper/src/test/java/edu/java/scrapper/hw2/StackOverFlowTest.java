package edu.java.scrapper.hw2;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.model.stack_over_flow.dto.AccountDTO;
import edu.java.model.stack_over_flow.dto.QuestionAnswerDTO;
import edu.java.model.stack_over_flow.dto.QuestionHeaderDTO;
import edu.java.service.StackOverFlowService;
import edu.java.service.client.StackOverFlowClient;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
public class StackOverFlowTest {
    String baseUrl = "http://localhost:";
    StackOverFlowClient client;
    int questionId = 1;
    private OffsetDateTime time = OffsetDateTime.of(2015, 12, 26, 12, 10, 15, 0, ZoneOffset.UTC);
    String title = "Lorem";

    @Test
    public void testHeader(WireMockRuntimeInfo wireMockRuntimeInfo) {

        client = new StackOverFlowService(baseUrl + wireMockRuntimeInfo.getHttpPort());
        String url = String.format("/questions/%d?order=desc&sort=activity&site=stackoverflow", questionId);
        AccountDTO owner = new AccountDTO(1, "Maxim");
        boolean isAnswered = false;
        String link = "sof.link";
        long lastEdit = 1451131815;
        QuestionHeaderDTO dto = new QuestionHeaderDTO(owner, isAnswered, lastEdit, title, questionId, link);

        WireMock.stubFor(
            WireMock.get(url)
                .willReturn(WireMock.aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(
                        """
                            {
                                "items":[
                                    {
                                       "owner": {
                                           "account_id": 20203,
                                           "user_id": 1,
                                           "display_name": "Maxim"

                                       },
                                       "is_answered": false,
                                       "view_count": 149172,

                                       "answer_count": 8,
                                       "score": 109,

                                       "creation_date": 1308483947,
                                       "last_edit_date": 1451131815,
                                       "question_id": 1,
                                       "content_license": "CC BY-SA 3.0",
                                       "link": "sof.link",
                                       "title": "Lorem"
                                    }
                                ]
                            }
                            """
                    ).withStatus(200)
                )
        );

        var response = client.fetchHeader(questionId).block();
        assertEquals(dto, response);
    }

    @Test
    public void testAnswers(WireMockRuntimeInfo wireMockRuntimeInfo) {

        client = new StackOverFlowService(baseUrl + wireMockRuntimeInfo.getHttpPort());
        String url = String.format("/questions/%d/answers?site=stackoverflow", questionId);
        AccountDTO owner = new AccountDTO(1, "Maxim");
        boolean isAccepted = false;
        long creationDate = 1451131815;
        long lastActivityDate = 1451131815;
        Long lastEdit = null;
        int answerId = 1;
        QuestionAnswerDTO dto =
            new QuestionAnswerDTO(owner, isAccepted, creationDate, lastActivityDate, lastEdit, answerId);
        var list = List.of(dto);
        WireMock.stubFor(
            WireMock.get(url)
                .willReturn(WireMock.aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(
                        """
                            {
                                "items":[
                                    {
                                       "owner": {
                                           "account_id": 12345,
                                           "reputation": 12864,
                                           "user_id": 1,
                                           "user_type": "registered",
                                           "profile_image": "https://www.gravatar.com/avatar/66fa6a7ac5ba2567c2ef30109ae989a6?s=256&d=identicon&r=PG",
                                           "display_name": "Maxim",
                                           "link": "https://stackoverflow.com/users/49505/joschi"
                                           },
                                       "is_accepted": false,
                                       "score": 147,
                                       "last_activity_date": 1451131815,
                                       "creation_date": 1451131815,
                                       "answer_id": 1,
                                       "question_id": 1
                                    }
                                ]

                            }
                            """
                    ).withStatus(200)
                )
        );

        var response = client.fetchAnswers(questionId).block();

        assertEquals(list, response);
    }
}
