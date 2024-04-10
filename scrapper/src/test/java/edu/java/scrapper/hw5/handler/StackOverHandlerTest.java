package edu.java.scrapper.hw5.handler;

import edu.java.model.StackOverFlowQuestionUriDto;
import edu.java.service.parser.StackOverFlowHandler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StackOverHandlerTest {

    private StackOverFlowHandler stackOverFlowHandler = new StackOverFlowHandler();

    @ParameterizedTest
    @CsvSource(value = {
        "https://stackoverflow.com/questions/12345/some-name",
        "https://stackoverflow.com/questions/1/name",
    })
    public void testCanHandleTrue(URI uri) {
        assertTrue(stackOverFlowHandler.canHandle(uri));
    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://stackoverflow.com/questions/",
        "https://github.com/owner/repo/pulls",
        "stackoverflow.com/help/10",
        "https://vk.com/questions/12345/pulls",
        "https://example.com",
    })
    public void testCanHandleFalse(URI uri) {
        assertFalse(stackOverFlowHandler.canHandle(uri));
    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://stackoverflow.com/questions/12345/some-name, 12345",
        "https://stackoverflow.com/questions/1/name, 1",
    })
    public void testHandle(URI uri, Integer questionId) {
        StackOverFlowQuestionUriDto response = (StackOverFlowQuestionUriDto) stackOverFlowHandler.handle(uri);
        StackOverFlowQuestionUriDto expected = new StackOverFlowQuestionUriDto(questionId);
        assertEquals(expected, response);

    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://stackoverflow.com/questions/",
        "https://github.com/owner/repo/pulls",
        "stackoverflow.com/help/10",
        "https://vk.com/questions/12345/pulls",
        "https://example.com",
    })
    public void testHandleException(URI uri) {
        assertThrows(IllegalArgumentException.class, () -> stackOverFlowHandler.handle(uri));
    }

}
