package edu.java.bot.exception;

import edu.java.bot.model.dto.response.ApiErrorResponse;
import lombok.Getter;

@Getter
public class ApiErrorException extends RuntimeException {

    private final ApiErrorResponse errorResponse;

    public ApiErrorException(ApiErrorResponse errorResponse) {
        super(errorResponse.getExceptionMessage());
        this.errorResponse = errorResponse;
    }

}
