package edu.java.model.stack_over_flow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AccountDto {
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("display_name")
    private String name;
}
