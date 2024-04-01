package edu.java.scrapper.hw5.handler;

import edu.java.exception.exception.IncorrectParametersException;
import edu.java.model.GitHubPullRequestUriDto;
import edu.java.service.handler.GitHubHandler;
import edu.java.model.GitHubPullRequestUriDto;
import edu.java.service.parser.GitHubHandler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GitHubHandlerTest {
    private GitHubHandler gitHubHandler = new GitHubHandler();

    @ParameterizedTest
    @CsvSource(value = {
        "https://github.com/onwer1/repo/pull/5",
        "https://github.com/owner2/blabla/pull/10"
    })
    public void testCanHandleTrue(URI uri) {
        assertTrue(gitHubHandler.canHandle(uri));
    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://github.com/owner/repo",
        "https://github.com/owner/repo/pulls",
        "github.com/help/me/pull/10",
        "https://vk.com/owner/repo/pulls",
        "https://example.com",
        "https://stackoverflow.com/questions/",
        "https://stackoverflow.com/questions/12345/some-name"
    })
    public void testCanHandleFalse(URI uri) {
        assertFalse(gitHubHandler.canHandle(uri));
    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://github.com/owner1/repo/pull/5, owner1, repo, 5",
        "https://github.com/owner2/blabla/pull/10, owner2, blabla, 10"
    })
    public void testHandle(URI uri, String owner, String repo, Integer number) {
        GitHubPullRequestUriDto response = (GitHubPullRequestUriDto) gitHubHandler.handle(uri);
        GitHubPullRequestUriDto expected = new GitHubPullRequestUriDto(owner, repo, number);
        assertEquals(expected, response);

    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://stacover.com/onwer1/repo/pull/5",
        "https://vklads.com/owner2/blabla/pull/10"
    })
    public void testHandleException(URI uri) {
        assertThrows(IncorrectParametersException.class,() ->gitHubHandler.handle(uri) );
    }
}
