package edu.java.bot.hw1.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CommandUtils;
import edu.java.bot.command.UnTrackCommand;
import edu.java.bot.exception.BotExceptionType;
import edu.java.bot.exception.LinkNotFoundException;
import edu.java.bot.exception.ListEmptyException;
import edu.java.bot.exception.UnsupportedSiteException;
import edu.java.bot.model.dto.response.ApiErrorResponse;
import edu.java.bot.processor.UserState;
import edu.java.bot.storage.Storage;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

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
    @Mock
    ApiErrorResponse apiErrorResponse;

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
        expectedString = CommandUtils.INPUT_URL_FOR_UN_TRACK;

        when(update.message().text()).thenReturn("/untrack");
        when(storage.getUserState(id)).thenReturn(UserState.DEFAULT);

        SendMessage response = unTrackCommand.handle(update);
        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get("text"));
    }

    @Test
    public void testUnTrackAwaitingUrlCheckException() {
        expectedString = CommandUtils.EXCEPTION_MESSAGE;
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
        expectedString = CommandUtils.URL_SUCCESSFULLY_REMOVED;
        String site = "abracadbra";

        when(storage.getUserState(id)).thenReturn(UserState.AWAITING_URL_FOR_UN_TRACK);
        when(update.message().text()).thenReturn(site);
        when(storage.removeUrl(id, site)).thenReturn(Optional.empty());

        SendMessage response = unTrackCommand.handle(update);
        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get("text"));
    }

    @Test
    public void testUnTrackRemoveUrlFailed() {
        expectedString = CommandUtils.URL_NOT_FOUND;
        String site = "abracadbra";

        when(storage.getUserState(id)).thenReturn(UserState.AWAITING_URL_FOR_UN_TRACK);
        when(update.message().text()).thenReturn(site);
        when(storage.removeUrl(id, site)).thenReturn(Optional.of(apiErrorResponse));
        when(apiErrorResponse.getExceptionName()).thenReturn(BotExceptionType.LINK_NOT_FOUND_EXCEPTION.name());

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
        expectedString = CommandUtils.AWAITING_URL;

        when(storage.getUserState(id)).thenReturn(UserState.AWAITING_URL_FOR_UN_TRACK);
        when(update.message().text()).thenReturn(site);
        lenient().when(storage.removeUrl(id, site)).thenReturn(Optional.empty());

        SendMessage response = unTrackCommand.handle(update);
        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get("text"));
    }
}
