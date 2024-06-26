package edu.java.scrapper.hw5bonus.processor;

import edu.java.domain.model.GitHubCommitDto;
import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.LinkRepository;
import edu.java.model.GitHubPullRequestUriDto;
import edu.java.model.github.PullRequestModelResponse;
import edu.java.model.github.dto.PullCommitDtoResponse;
import edu.java.model.github.dto.info.UserInfoDto;
import edu.java.service.client.GitHubClient;
import edu.java.service.database.GitHubService;
import edu.java.service.processor.GitHubProcessor;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GithubProcessorTest {
    public static final long TG_CHAT_ID = 1l;
    @Mock
    GitHubService gitHubService;
    @Mock
    LinkRepository linkRepository;
    @Mock
    GitHubClient gitHubClient;

    PullRequestModelResponse mockResponse;
    LinkDto linkDTO;
    OffsetDateTime time = OffsetDateTime.of(2015, 1, 1, 1, 1, 1, 0, ZoneOffset.UTC);

    @Test
    public void testUriProcessor() {
        mockResponse = new PullRequestModelResponse();
        List<PullCommitDtoResponse> expectedListCommit = List.of(
            new PullCommitDtoResponse("shashashasha",
                "html",
                new PullCommitDtoResponse.CommitDetail(new PullCommitDtoResponse.CommitDetail.CommitDetailInfo(
                    "name",
                    "mail",
                    time
                ), "message"
                ), new UserInfoDto("login", 1l)
            ),
            new PullCommitDtoResponse("abababababba",
                "html2",
                new PullCommitDtoResponse.CommitDetail(new PullCommitDtoResponse.CommitDetail.CommitDetailInfo(
                    "name2",
                    "mail2",
                    time
                ), "message2"
                ), new UserInfoDto("login2", 2l)
            )
        );
        mockResponse.setPullCommitDTOS(expectedListCommit);
        linkDTO = new LinkDto();
        linkDTO.setTgChatId(TG_CHAT_ID);
        linkDTO.setUri(URI.create("https://github.com/lsn03/java-2023-backend-second-semester/pull/5"));
        linkDTO.setCreatedAt(time);
        linkDTO.setLastUpdate(time);
        List<GitHubCommitDto> listFromDb = List.of(
            GitHubCommitDto.create(expectedListCommit.getFirst())
        );

        when(gitHubService.addCommits(anyList())).thenReturn(1);
        when(gitHubService.getCommits(any(URI.class))).thenReturn(listFromDb);
        when(gitHubClient.fetchPullRequest(anyString(), anyString(), anyInt())).thenReturn(mockResponse);
        lenient().doNothing().when(linkRepository).updateLink(any(LinkDto.class));
        GitHubProcessor gitHubProcessor = new GitHubProcessor(gitHubClient, gitHubService, linkRepository);

        var responseList = gitHubProcessor.processUriDTO(linkDTO, new GitHubPullRequestUriDto("", "", 0));

        assertTrue(responseList.getFirst().getDescription().contains("Пользователь"));
    }
}
