package edu.java.model.stack_over_flow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuestionHeaderDTO {
    @JsonProperty("is_answered")
    private Boolean isAnswered;

    @JsonProperty("last_edit_date")
    private OffsetDateTime lastEdit;

    private String title;

    @JsonProperty("question_id")
    private int questionId;
}
