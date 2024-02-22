package edu.java.exception;

import edu.java.exception.exception.IncorrectParametersException;
import edu.java.exception.exception.LinkNotFoundException;
import edu.java.exception.exception.ListEmptyException;
import edu.java.exception.exception.RepeatTrackException;
import edu.java.exception.exception.UserAlreadyExistException;
import edu.java.exception.exception.UserDoesntExistException;
import edu.java.model.scrapper.dto.ApiErrorResponse;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(RepeatTrackException.class.getName());
        errorResponse.setDescription(ERROR_LINK_ALREADY_TRACKED);
        errorResponse.setStacktrace(Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(
            Collectors.toList()));
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkNotFoundException(LinkNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(LinkNotFoundException.class.getName());
        errorResponse.setDescription(ERROR_LINK_NOT_FOUND);

        errorResponse.setStacktrace(Arrays.stream(ex.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.toList()));
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(UserAlreadyExistException.class.getName());
        errorResponse.setDescription(ERROR_CHAT_ALREADY_EXIST);

        errorResponse.setStacktrace(Arrays.stream(ex.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.toList()));
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(UserDoesntExistException.class)
    public ResponseEntity<ApiErrorResponse> handleUserDoesntExistException(UserDoesntExistException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(UserDoesntExistException.class.getName());
        errorResponse.setDescription(ERROR_CHAT_NOT_EXIST);

        errorResponse.setStacktrace(Arrays.stream(ex.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.toList()));
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(ListEmptyException.class)
    public ResponseEntity<ApiErrorResponse> handleListEmptyException(ListEmptyException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(ListEmptyException.class.getName());
        errorResponse.setDescription(ERROR_LIST_EMPTY);

        errorResponse.setStacktrace(Arrays.stream(ex.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.toList()));
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(IncorrectParametersException.class)
    public ResponseEntity<ApiErrorResponse> handleIncorrectParametersException(IncorrectParametersException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(IncorrectParametersException.class.getName());
        errorResponse.setDescription(ERROR_INCORRECT_PARAMETERS);

        errorResponse.setStacktrace(Arrays.stream(ex.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.toList()));
        return ResponseEntity.status(status).body(errorResponse);
    }
}
