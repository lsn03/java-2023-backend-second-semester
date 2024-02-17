package edu.java.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestToGitHub {
    private String url;

    @JsonProperty("chat_id")
    private Long chatId;

    private String owner;

    private String repo;

    @JsonProperty("pull_number")
    private Long pullNumber;

}
