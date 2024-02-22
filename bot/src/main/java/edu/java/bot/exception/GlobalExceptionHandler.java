package edu.java.bot.exception;

import edu.java.bot.model.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final String ERROR_INCORRECT_PARAMETERS = "Некорректные параметры запроса";
    @ExceptionHandler(IncorrectParametersException.class)
    public ResponseEntity<ApiErrorResponse> handleIncorrectParametersException(IncorrectParametersException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(IncorrectParametersException.class.getSimpleName());
        errorResponse.setDescription(ERROR_INCORRECT_PARAMETERS);

        errorResponse.setStacktrace(Arrays.stream(ex.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.toList()));
        return ResponseEntity.status(status).body(errorResponse);
    }
}
