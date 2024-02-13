package edu.java.bot.hw1.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.StartCommand;
import edu.java.bot.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StartCommandTest {
    private static final String TEXT = "text";
    private Long id = 12345L;
    private Update update;
    private Message message;
    private Chat chat;
    @Mock
    private Storage storage;
    @InjectMocks
    private StartCommand startCommand;
    private String command = "/start";

    @BeforeEach
    public void setUp() {
        update = mock(Update.class);
        message = mock(Message.class);
        chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);
    }

    @Test
    public void testUserRegisteredStart() {
        lenient().when(storage.isUserAuth(id)).thenReturn(false);
        when(message.text()).thenReturn(command);
        SendMessage sendMessage = startCommand.handle(update);
        assertNotNull(sendMessage);
        assertEquals(StartCommand.USER_REGISTERED_SUCCESS, sendMessage.getParameters().get(TEXT));
    }

    @Test
    public void testUserAlreadyRegisteredStart() {
        lenient().when(storage.isUserAuth(id)).thenReturn(true);
        when(message.text()).thenReturn(command);
        SendMessage sendMessage = startCommand.handle(update);
        assertNotNull(sendMessage);
        assertEquals(StartCommand.ALREADY_AUTH, sendMessage.getParameters().get(TEXT));
    }
}
