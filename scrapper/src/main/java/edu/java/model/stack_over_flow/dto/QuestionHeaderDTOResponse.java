package edu.java.model.stack_over_flow.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class QuestionHeaderDTOResponse {

    private AccountDTO owner;

    @JsonProperty("is_answered")
    private boolean isAnswered;

    @JsonProperty("last_edit_date")
    private OffsetDateTime lastEdit;

    private String title;

    @JsonProperty("question_id")
    private int questionId;

    private String link;

    @JsonCreator
    public QuestionHeaderDTOResponse(
        AccountDTO owner,
        @JsonProperty("is_answered") boolean isAnswered,
        @JsonProperty("last_edit_date") long lastEdit,
        String title,
        @JsonProperty("question_id") int questionId,
        String link
    ) {
        this.owner = owner;
        this.isAnswered = isAnswered;
        this.lastEdit = Instant.ofEpochSecond(lastEdit).atOffset(ZoneOffset.UTC);
        this.title = title;
        this.questionId = questionId;
        this.link = link;
    }

    public void setCreationDate(OffsetDateTime time) {
        lastEdit = time;
    }

    public void setCreationDate(long time) {
        lastEdit = Instant.ofEpochSecond(time).atOffset(ZoneOffset.UTC);
    }
}
