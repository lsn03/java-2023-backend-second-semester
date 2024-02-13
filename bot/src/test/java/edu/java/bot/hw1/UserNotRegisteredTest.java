package edu.java.bot.hw1;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CancelCommand;
import edu.java.bot.command.Command;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UnTrackCommand;
import edu.java.bot.command.UnknownCommand;
import edu.java.bot.storage.Storage;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
