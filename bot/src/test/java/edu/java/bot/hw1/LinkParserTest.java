package edu.java.bot.hw1;

import edu.java.bot.exception.UnsupportedSiteException;
import edu.java.bot.parser.GitHubHandler;
import edu.java.bot.parser.ResourceHandler;
import edu.java.bot.parser.StackOverFlowHandler;
import edu.java.bot.service.LinkParserService;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LinkParserTest {
    private List<ResourceHandler> resourceHandlerList = List.of(
        new StackOverFlowHandler(),
        new GitHubHandler()
    );
    private LinkParserService parserService = new LinkParserService(resourceHandlerList);
    ;

    @ParameterizedTest
    @CsvSource(value = {
        "https://github.com/lsn03/java-2023-backend-second-semester/pull/1",
        "https://github.com/lsn03/",
        "https://github.com/lsn03/java-2023-backend-second-semester/pull/1/commits",
        "https://stackoverflow.com/questions/28458846/how-to-require-tab-indentation-with-checkstyle",
        "https://stackoverflow.com/questions",
    })
    public void validUrl(String url) {
        assertDoesNotThrow(() -> parserService.process(url));
    }

    @ParameterizedTest
    @CsvSource(value = {
        "github.com/lsn03/java-2023-backend-second-semester/pull/1",
        "github.com/lsn03/",
        "github.com/lsn03/java-2023-backend-second-semester/pull/1/commits",
        "stackoverflow.com/questions/28458846/how-to-require-tab-indentation-with-checkstyle",
        "stackoverflow.com/questions",
        "aboba",
        "avadacedavra",
        "/list",
    })
    public void inValidUrl(String url) {
        assertThrows(UnsupportedSiteException.class, () -> parserService.process(url));
    }
}
