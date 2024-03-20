package edu.java.bot.exception;

import edu.java.bot.model.dto.response.ApiErrorResponse;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String ERROR_INCORRECT_PARAMETERS = "Некорректные параметры запроса";



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

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setCode(String.valueOf(status.value()));
        errorResponse.setExceptionMessage(ex.getMessage());
        errorResponse.setExceptionName(NoResourceFoundException.class.getSimpleName());
        errorResponse.setDescription("NOT FOUND");

        errorResponse.setStacktrace(Arrays.stream(ex.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.toList()));
        return ResponseEntity.status(status).body(errorResponse);
    }
}
