package edu.java.bot.hw1.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.UnknownCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnknownCommandTest {
    @InjectMocks
    private UnknownCommand unknownCommand;
    private static final String TEXT = "text";
    private long id = 12345L;
    private Update update;
    private Message message;
    private Chat chat;

    @BeforeEach
    public void setUp() {
        update = mock(Update.class);
        message = mock(Message.class);
        chat = mock(Chat.class);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "aboba",
        "/pomogite",
        "/avadocedavra",
        "there is no help"
    })
    public void testUserUnknownCommand(String command) {
        String expectedString = UnknownCommand.UNKNOWN_COMMAND_HELP;

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);
        when(message.text()).thenReturn(command);

        SendMessage response = unknownCommand.handle(update);

        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get(TEXT));

    }
}
