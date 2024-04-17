package edu.java.exception;

import edu.java.exception.exception.IncorrectParametersException;
import edu.java.exception.exception.LinkNotFoundException;
import edu.java.exception.exception.ListEmptyException;
import edu.java.exception.exception.RepeatTrackException;
import edu.java.exception.exception.UserAlreadyExistException;
import edu.java.exception.exception.UserDoesntExistException;
import edu.java.model.scrapper.dto.response.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final String ERROR_INCORRECT_PARAMETERS = "Некорректные параметры запроса";
    public static final String ERROR_LINK_NOT_FOUND = "Ссылка не найдена";
    public static final String ERROR_LINK_ALREADY_TRACKED = "Ссылка уже отслеживается";
    public static final String ERROR_LIST_EMPTY = "Список отслеживаемых ссылок пуст";
    public static final String ERROR_CHAT_NOT_EXIST = "Чат не существует";
    public static final String ERROR_CHAT_ALREADY_EXIST = "Чат уже существует";

    @ExceptionHandler(RepeatTrackException.class)
    public ResponseEntity<ApiErrorResponse> handleRepeatTrackException(RepeatTrackException ex) {
        HttpStatus status = HttpStatus.CONFLICT;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(BotExceptionType.REPEAT_TRACK_EXCEPTION.name());
        errorResponse.setDescription(ERROR_LINK_ALREADY_TRACKED);
        errorResponse.setStacktrace(stacktraceArrayToListString(ex.getStackTrace()));
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkNotFoundException(LinkNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(BotExceptionType.LINK_NOT_FOUND_EXCEPTION.name());
        errorResponse.setDescription(ERROR_LINK_NOT_FOUND);

        errorResponse.setStacktrace(stacktraceArrayToListString(ex.getStackTrace()));
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        HttpStatus status = HttpStatus.CONFLICT;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(BotExceptionType.USER_ALREADY_EXIST_EXCEPTION.name());
        errorResponse.setDescription(ERROR_CHAT_ALREADY_EXIST);

        errorResponse.setStacktrace(stacktraceArrayToListString(ex.getStackTrace()));
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(UserDoesntExistException.class)
    public ResponseEntity<ApiErrorResponse> handleUserDoesntExistException(UserDoesntExistException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(BotExceptionType.USER_DOESNT_EXIST_EXCEPTION.name());
        errorResponse.setDescription(ERROR_CHAT_NOT_EXIST);

        errorResponse.setStacktrace(stacktraceArrayToListString(ex.getStackTrace()));
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(ListEmptyException.class)
    public ResponseEntity<ApiErrorResponse> handleListEmptyException(ListEmptyException ex) {
        HttpStatus status = HttpStatus.CONFLICT;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(BotExceptionType.LIST_EMPTY_EXCEPTION.name());
        errorResponse.setDescription(ERROR_LIST_EMPTY);

        errorResponse.setStacktrace(stacktraceArrayToListString(ex.getStackTrace()));
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(IncorrectParametersException.class)
    public ResponseEntity<ApiErrorResponse> handleIncorrectParametersException(IncorrectParametersException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(BotExceptionType.INCORRECT_PARAMETERS_EXCEPTION.name());
        errorResponse.setDescription(ERROR_INCORRECT_PARAMETERS);

        errorResponse.setStacktrace(stacktraceArrayToListString(ex.getStackTrace()));
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(NoResourceFoundException.class.getSimpleName());
        errorResponse.setDescription(status.getReasonPhrase());

        errorResponse.setStacktrace(stacktraceArrayToListString(ex.getStackTrace()));
        return ResponseEntity.status(status).body(errorResponse);
    }

    @NotNull
    private static List<String> stacktraceArrayToListString(StackTraceElement[] ex) {
        return Arrays.stream(ex)
            .map(StackTraceElement::toString)
            .collect(Collectors.toList());
    }
}
