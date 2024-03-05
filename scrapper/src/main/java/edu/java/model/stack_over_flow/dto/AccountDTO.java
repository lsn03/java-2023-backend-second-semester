package edu.java.model.stack_over_flow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AccountDTO {
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("display_name")
    private String name;
}
