package edu.java.bot.hw1.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.storage.Storage;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class HelpCommandTest {
    private static final String TEXT = "text";
    private Long id = 12345L;

    private String expectedString;
    @Mock
    private Storage storage;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @Mock
    private Command mockCommand;

    private HelpCommand helpCommand;
    private List<Command> commandList;

    @BeforeEach
    public void setUp() {

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);

        commandList = new ArrayList<>();
        commandList.add(mockCommand);
        when(mockCommand.command()).thenReturn("/test");
        when(mockCommand.description()).thenReturn("Тестовая команда");
        helpCommand = new HelpCommand(commandList, storage);

    }

    @Test
    public void test() {

        lenient().when(storage.isUserAuth(id)).thenReturn(true);
        SendMessage sendMessage = helpCommand.handle(update);
        assertNotNull(sendMessage);
        String mess = (String) sendMessage.getParameters().get(TEXT);
        assertTrue(mess.contains(mockCommand.command()));
        assertTrue(mess.contains(mockCommand.description()));
    }
}
