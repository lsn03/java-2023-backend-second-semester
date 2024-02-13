package edu.java.bot.hw1;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.*;
import edu.java.bot.processor.UserMessageProcessor;
import edu.java.bot.service.LinkParserService;
import edu.java.bot.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserNotRegisteredTest {
    private static final String TEXT = "text";
    long id = 12345L;
    Update update;
    Message message;
    Chat chat;
    String expectedString;
    @Mock
    private Storage storage;

    @InjectMocks
    private StartCommand startCommand;
    @InjectMocks
    private CancelCommand cancelCommand;
    @InjectMocks
    private HelpCommand helpCommand;
    @InjectMocks
    private ListCommand listCommand;
    @InjectMocks
    private TrackCommand trackCommand;
    @InjectMocks
    private UnTrackCommand unTrackCommand;
    @InjectMocks
    private UnknownCommand unknownCommand;

    @Mock
    private LinkParserService parserService;
    @Mock
    private UserMessageProcessor userMessageProcessor;
    @Mock
    List<Command> commandList;

    @BeforeEach
    public void setUp() {
        storage = mock(Storage.class);
        parserService = mock(LinkParserService.class);
        commandList = new ArrayList<>();

        commandList.add(cancelCommand);
        commandList.add(helpCommand);
        commandList.add(listCommand);
        commandList.add(trackCommand);
        commandList.add(unTrackCommand);
        commandList.add(unknownCommand);

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

    @Test
    public void testUserNotRegisteredCancel() {
        expectedString = CancelCommand.USER_NOT_REGISTERED;
        Command currentCommand = cancelCommand;
        defaultInit(id, currentCommand.command());
        lenient().when(storage.isUserAuth(id)).thenReturn(false);
        SendMessage response = currentCommand.handle(update);

        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get(TEXT));

    }

    @Test
    public void testUserNotRegisteredHelp() {
        expectedString = HelpCommand.USER_NOT_REGISTERED;
        Command currentCommand = helpCommand;
        defaultInit(id, currentCommand.command());
        lenient().when(storage.isUserAuth(id)).thenReturn(false);
        SendMessage response = currentCommand.handle(update);

        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get(TEXT));

    }

    @Test
    public void testUserNotRegisteredList() {
        expectedString = ListCommand.USER_NOT_REGISTERED;
        Command currentCommand = listCommand;
        defaultInit(id, currentCommand.command());
        lenient().when(storage.isUserAuth(id)).thenReturn(false);
        SendMessage response = currentCommand.handle(update);

        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get(TEXT));

    }

    @Test
    public void testUserNotRegisteredTrack() {
        expectedString = ListCommand.USER_NOT_REGISTERED;
        Command currentCommand = trackCommand;
        defaultInit(id, currentCommand.command());
        lenient().when(storage.isUserAuth(id)).thenReturn(false);
        SendMessage response = currentCommand.handle(update);

        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get(TEXT));

    }

    @Test
    public void testUserNotRegisteredUnTrack() {
        expectedString = ListCommand.USER_NOT_REGISTERED;
        Command currentCommand = unTrackCommand;
        defaultInit(id, currentCommand.command());
        lenient().when(storage.isUserAuth(id)).thenReturn(false);
        SendMessage response = currentCommand.handle(update);

        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get(TEXT));

    }

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
