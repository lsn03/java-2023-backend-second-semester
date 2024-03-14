package edu.java.bot.hw1.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.command.CancelCommand;
import edu.java.bot.command.CommandUtils;
import edu.java.bot.processor.UserState;
import edu.java.bot.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CancelCommandTest {
    long id = 12345L;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Storage storage;
    private CancelCommand cancelCommand;
    private String expectedString;

    @BeforeEach
    public void setUp() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);

        lenient().doNothing().when(storage).setUserState(anyLong(), any(UserState.class));
        when(storage.isUserAuth(id)).thenReturn(true);
        cancelCommand = new CancelCommand(storage);
    }

    @ParameterizedTest
    @EnumSource(value = UserState.class, names = {"DEFAULT", "UNAUTHORIZED"})
    public void nothingToCancelTest(UserState state) {
        expectedString = CommandUtils.NOTHING_TO_CANCEL;
        when(storage.getUserState(id)).thenReturn(state);

        var response = cancelCommand.handle(update);
        assertEquals(expectedString, response.getParameters().get("text"));
    }

    @ParameterizedTest
    @EnumSource(value = UserState.class, names = {"AWAITING_URL_FOR_TRACK", "AWAITING_URL_FOR_UN_TRACK"})
    public void testCancelInput(UserState state) {
        expectedString = CommandUtils.CANCEL_INPUT;
        when(storage.getUserState(id)).thenReturn(state);

        var response = cancelCommand.handle(update);
        assertEquals(expectedString, response.getParameters().get("text"));
    }

}
