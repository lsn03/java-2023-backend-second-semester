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
    private long id = 12345L;
    private Update update;
    private Message message;
    private Chat chat;
    private String expectedString;
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
    List<Command> commandList;

    @BeforeEach
    public void setUp() {

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

}
