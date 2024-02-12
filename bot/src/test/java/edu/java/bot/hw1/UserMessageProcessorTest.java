package edu.java.bot.hw1;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.processor.UserMessageProcessor;
import edu.java.bot.service.CommandService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMessageProcessorTest {
    Update update;
    Message message;
    Chat chat;

    @Mock
    private CommandService commandService;
    @Mock
    private List<Command> commandList;

    @InjectMocks
    private UserMessageProcessor userMessageProcessor;

    @BeforeEach
    public void initMockClasses() {
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

    @ParameterizedTest()
    @CsvSource(value = {
        "/cancel",
        "/track",
        "/untrack",
        "/list",
        "/aboba",
        "/help",
        "hello"
    })
    public void testProcessUserNotRegistered(String command) {
        String expected = UserMessageProcessor.USER_NOT_REGISTERED;
        defaultInit(12345L, command);
        SendMessage response = userMessageProcessor.process(update);

        assertNotNull(response);
        assertEquals(expected, response.getParameters().get("text"));

    }
}
