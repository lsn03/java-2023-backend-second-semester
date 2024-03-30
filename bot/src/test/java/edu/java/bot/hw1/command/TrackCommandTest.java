package edu.java.bot.hw1.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CommandUtils;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.exception.BotExceptionType;
import edu.java.bot.exception.RepeatTrackException;
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
public class TrackCommandTest {
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

    private TrackCommand trackCommand;
    private String expectedString;

    @BeforeEach
    public void setUp() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);

        lenient().doNothing().when(storage).setUserState(anyLong(), any(UserState.class));
        when(storage.isUserAuth(id)).thenReturn(true);
        trackCommand = new TrackCommand(storage);
    }

    @Test
    public void testTrackFirstTIme() {
        expectedString = CommandUtils.INPUT_URL_FOR_TRACK;

        when(update.message().text()).thenReturn("/track");
        when(storage.getUserState(id)).thenReturn(UserState.DEFAULT);

        SendMessage response = trackCommand.handle(update);
        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get("text"));
    }

    @Test
    public void testTrackAwaitingUrlCheckException() {
        expectedString = CommandUtils.EXCEPTION_MESSAGE;
        String site = "abracadbra";

        when(storage.getUserState(id)).thenReturn(UserState.AWAITING_URL_FOR_TRACK);
        when(update.message().text()).thenReturn(site);
        doThrow(new UnsupportedSiteException("")).when(storage).addUrl(id, site);

        SendMessage response = trackCommand.handle(update);
        assertNotNull(response);
        assertTrue(((String) response.getParameters().get("text")).contains(expectedString));
    }

    @Test
    public void testTrackAddUrlSuccess() {
        expectedString = CommandUtils.URL_SUCCESSFULLY_ADDED;
        String site = "abracadbra";

        when(storage.getUserState(id)).thenReturn(UserState.AWAITING_URL_FOR_TRACK);
        when(update.message().text()).thenReturn(site);
        when(storage.addUrl(id, site)).thenReturn(Optional.empty());

        SendMessage response = trackCommand.handle(update);
        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get("text"));
    }

    @Test
    public void testTrackAddUrlFailed() {
        expectedString = CommandUtils.URL_ALREADY_EXIST;
        String site = "abracadbra";

        when(storage.getUserState(id)).thenReturn(UserState.AWAITING_URL_FOR_TRACK);
        when(update.message().text()).thenReturn(site);
        when(storage.addUrl(id, site)).thenReturn(Optional.of(apiErrorResponse));
        when(apiErrorResponse.getExceptionName()).thenReturn(BotExceptionType.REPEAT_TRACK_EXCEPTION.name());
        SendMessage response = trackCommand.handle(update);
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
    public void testTrackAddUrlFailedEnterCommand(String site) {
        expectedString = CommandUtils.AWAITING_URL;

        when(storage.getUserState(id)).thenReturn(UserState.AWAITING_URL_FOR_TRACK);
        when(update.message().text()).thenReturn(site);
        lenient().when(storage.addUrl(id, site)).thenReturn(Optional.empty());

        SendMessage response = trackCommand.handle(update);
        assertNotNull(response);
        assertEquals(expectedString, response.getParameters().get("text"));
    }
}
