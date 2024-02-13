package edu.java.bot.hw1.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.UnTrackCommand;
import edu.java.bot.exception.UnsupportedSiteException;
import edu.java.bot.processor.UserState;
import edu.java.bot.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnTrackCommandTest {
    long id = 12345L;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Storage storage;
    private UnTrackCommand unTrackCommand;
    private String expectedString;

    @BeforeEach
    public void setUp() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);

        lenient().doNothing().when(storage).setUserState(anyLong(), any(UserState.class));
        when(storage.isUserAuth(id)).thenReturn(true);
        unTrackCommand = new UnTrackCommand(storage);
    }

    @Test
    public void testUnTrackFirstTIme() {
        expectedString = UnTrackCommand.INPUT_URL_FOR_UN_TRACK;

        when(update.message().text()).thenReturn("/untrack");
        when(storage.getUserState(id)).thenReturn(UserState.DEFAULT);

        SendMessage response = unTrackCommand.handle(update);
        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get("text"));
    }

    @Test
    public void testUnTrackAwaitingUrlCheckException() {
        expectedString = UnTrackCommand.EXCEPTION_MESSAGE;
        String site = "abracadbra";

        when(storage.getUserState(id)).thenReturn(UserState.AWAITING_URL_FOR_UN_TRACK);
        when(update.message().text()).thenReturn(site);
        doThrow(new UnsupportedSiteException("")).when(storage).removeUrl(id, site);

        SendMessage response = unTrackCommand.handle(update);
        assertNotNull(response);
        assertTrue(((String) response.getParameters().get("text")).contains(expectedString));
    }

    @Test
    public void testUnTrackRemoveUrlSuccess() {
        expectedString = UnTrackCommand.URL_SUCCESSFULLY_REMOVED;
        String site = "abracadbra";

        when(storage.getUserState(id)).thenReturn(UserState.AWAITING_URL_FOR_UN_TRACK);
        when(update.message().text()).thenReturn(site);
        when(storage.removeUrl(id, site)).thenReturn(true);

        SendMessage response = unTrackCommand.handle(update);
        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get("text"));
    }

    @Test
    public void testUnTrackRemoveUrlFailed() {
        expectedString = UnTrackCommand.URL_NOT_FOUND;
        String site = "abracadbra";

        when(storage.getUserState(id)).thenReturn(UserState.AWAITING_URL_FOR_UN_TRACK);
        when(update.message().text()).thenReturn(site);
        when(storage.removeUrl(id, site)).thenReturn(false);

        SendMessage response = unTrackCommand.handle(update);
        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get("text"));
    }

    @ParameterizedTest
    @CsvSource(value = {
        "/list",
        "/track",
        "/untrack",
        "/start",
        "/help",
    })
    public void testUnTrackRemoveUrlFailedEnterCommand(String site) {
        expectedString = UnTrackCommand.AWAITING_URL;

        when(storage.getUserState(id)).thenReturn(UserState.AWAITING_URL_FOR_UN_TRACK);
        when(update.message().text()).thenReturn(site);
        lenient().when(storage.removeUrl(id, site)).thenReturn(true);

        SendMessage response = unTrackCommand.handle(update);
        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get("text"));
    }
}
