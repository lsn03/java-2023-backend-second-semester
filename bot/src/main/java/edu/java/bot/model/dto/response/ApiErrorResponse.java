package edu.java.bot.model.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse extends MyResponse {

    private String description;
    private String code;
    private String exceptionName;
    private String exceptionMessage;
    private List<String> stacktrace;

}
