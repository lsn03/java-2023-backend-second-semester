package edu.java.bot.hw1.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CommandUtils;
import edu.java.bot.command.ListCommand;
import edu.java.bot.model.dto.response.LinkResponse;
import edu.java.bot.storage.Storage;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCommandTest {
    private static final String TEXT = "text";
    private Long id = 12345L;

    @Mock
    private Storage storage;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;
    private ListCommand listCommand;

    @BeforeEach
    public void setUp() {

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);

        listCommand = new ListCommand(storage);

        when(storage.isUserAuth(id)).thenReturn(true);
    }

    @Test
    public void listIsNotEmpty() {
        var example = new LinkResponse(id, URI.create("https://github.com/"));
        when(storage.getUserTracks(id)).thenReturn(List.of(example));
        SendMessage response = listCommand.handle(update);
        assertNotNull(response);
        var text = (String) response.getParameters().get(TEXT);
        assertFalse(text.isEmpty());
        assertTrue(text.contains(example.getUrl().toString()));
    }

    @Test
    public void listIsEmpty() {
        when(storage.getUserTracks(id)).thenReturn(List.of());
        SendMessage response = listCommand.handle(update);
        assertNotNull(response);
        var text = (String) response.getParameters().get(TEXT);
        assertEquals(CommandUtils.NOTHING_TO_TRACK, text);
    }
}
