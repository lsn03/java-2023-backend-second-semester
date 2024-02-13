package edu.java.bot.hw1;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.command.Command;
import edu.java.bot.parser.GitHubHandler;
import edu.java.bot.parser.ResourceHandler;
import edu.java.bot.parser.StackOverFlowHandler;
import edu.java.bot.processor.UserMessageProcessor;
import edu.java.bot.service.CommandService;
import java.util.ArrayList;
import java.util.List;
import edu.java.bot.service.LinkParserService;
import edu.java.bot.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMessageProcessorTest {
    public static final String TEXT = "text";
    long id = 12345L;
    Update update;
    Message message;
    Chat chat;
    String expectedString;



    @Mock
    private Command startCommand;
    @Mock
    private Command cancelCommand;
    @Mock
    private Command helpCommand;
    @Mock
    private Command listCommand;
    @Mock
    private Command trackCommand;
    @Mock
    private Command unTrackCommand;
    @Mock
    private Storage storage;
    @Mock
    private ResourceHandler github = new GitHubHandler();
    @Mock
    private ResourceHandler stackOverFlow = new StackOverFlowHandler();
    @Mock
    private LinkParserService parserService;
    private CommandService commandService;
    private UserMessageProcessor userMessageProcessor;

    @BeforeEach
    public void setUp() {

        List<Command> commandList = new ArrayList<>();
        lenient().when(startCommand.command()).thenReturn("/start");
        lenient().when(cancelCommand.command()).thenReturn("/cancel");
        lenient().when(helpCommand.command()).thenReturn("/help");
        lenient().when(listCommand.command()).thenReturn("/list");
        lenient().when(trackCommand.command()).thenReturn("/track");
        lenient().when(unTrackCommand.command()).thenReturn("/untrack");


        commandList.add(cancelCommand);
        commandList.add(helpCommand);
        commandList.add(listCommand);
        commandList.add(trackCommand);
        commandList.add(unTrackCommand);





        List<ResourceHandler> resourceHandlerList = new ArrayList<>();
        resourceHandlerList.add(github);
        resourceHandlerList.add(stackOverFlow);

        parserService = new LinkParserService(resourceHandlerList);
        commandService = new CommandService(storage,parserService);
        userMessageProcessor = new UserMessageProcessor(commandList, storage);

        update = mock(Update.class);
        message = mock(Message.class);
        chat = mock(Chat.class);
    }

    public void defaultInit(Long id, String command) {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);
        when(message.text()).thenReturn(command);
    }


//    @ParameterizedTest()
//    @CsvSource(value = {
//        "/cancel",
//        "/track",
//        "/untrack",
//        "/list",
//        "/aboba",
//        "/help",
//        "hello"
//    })
//    public void testProcessUserNotRegistered(String command) {
//        expectedString = UserMessageProcessor.USER_NOT_REGISTERED;
//        defaultInit(id, command);
//        SendMessage response = userMessageProcessor.process(update);
//
//        assertNotNull(response);
//        assertEquals(expectedString, response.getParameters().get(TEXT));
//
//    }
//
//    @Test
//    public void testStartCommand() {
//        expectedString = UserMessageProcessor.USER_REGISTERED_SUCCESS;
//        defaultInit(id, "/start");
//        SendMessage response = userMessageProcessor.process(update);
//
//        assertNotNull(response);
//        assertEquals(expectedString, response.getParameters().get(TEXT));
//    }
//
//    @Test
//    public void testTrackCommand() {
//        expectedString = TrackCommand.INPUT_URL_FOR_TRACK;
//        defaultInit(id, "/start");
//        userMessageProcessor.process(update);
//        defaultInit(id, "/track");
//        when(trackCommand.handle(update)).thenReturn(new SendMessage(id, TrackCommand.INPUT_URL_FOR_TRACK));
//        SendMessage response = userMessageProcessor.process(update);
//        assertNotNull(response);
//        assertEquals(expectedString, response.getParameters().get(TEXT));
//    }
//
//    @Test
//    public void testAddUrl() {
//        testTrackCommand();
//
//        expectedString = UserMessageProcessor.URL_SUCCESSFULLY_ADDED;
//        defaultInit(id, "https://github.com/lsn03");
//        when(storage.addUrl(id,"https://github.com/lsn03")).thenReturn(true);
//        SendMessage response = userMessageProcessor.process(update);
//        assertEquals(expectedString, response.getParameters().get(TEXT));
//    }
//    @Test
//    public void testListUrlEmpty() {
//        expectedString = ListCommand.NOTHING_TO_TRACK;
//        defaultInit(id, "/start");
//        userMessageProcessor.process(update);
//        defaultInit(id, "/list");
//        when(listCommand.handle(update)).thenReturn(new SendMessage(id,expectedString));
//        SendMessage response = userMessageProcessor.process(update);
//        assertEquals(expectedString, response.getParameters().get(TEXT));
//    }
}
