package edu.java.bot.hw1;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CancelCommand;
import edu.java.bot.command.Command;
import edu.java.bot.command.CommandUtils;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UnTrackCommand;
import edu.java.bot.command.UnknownCommand;
import edu.java.bot.storage.Storage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    private String expectedString;
    @Mock
    private Storage storage;

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

    static Stream<Command> commandProvider() {
        Storage storage1 = mock(Storage.class);

        return Stream.of(
            new CancelCommand(storage1),
            new HelpCommand(new ArrayList<>(), storage1),
            new ListCommand(storage1),
            new UnTrackCommand(storage1),
            new TrackCommand(storage1)
        );
    }

    @BeforeEach
    public void setUp() {

        commandList = new ArrayList<>();

        commandList.add(cancelCommand);
        commandList.add(helpCommand);
        commandList.add(listCommand);
        commandList.add(trackCommand);
        commandList.add(unTrackCommand);
        commandList.add(unknownCommand);

    }

    public void defaultInit(Long id, String command) {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);
        when(message.text()).thenReturn(command);
    }

    @ParameterizedTest
    @MethodSource("commandProvider")
    public void testUserNotRegisteredCommands(Command command) {
        expectedString = CommandUtils.USER_NOT_REGISTERED;
        defaultInit(id, command.command());
        lenient().when(storage.isUserAuth(id)).thenReturn(false);
        SendMessage response = command.handle(update);

        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get(TEXT));
    }

    @Test
    public void testUserNotRegisteredCancel() {
        expectedString = CommandUtils.UNKNOWN_COMMAND_HELP;
        Command currentCommand = unknownCommand;
        defaultInit(id, currentCommand.command());
        lenient().when(storage.isUserAuth(id)).thenReturn(false);
        SendMessage response = currentCommand.handle(update);

        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get(TEXT));

    }

}
