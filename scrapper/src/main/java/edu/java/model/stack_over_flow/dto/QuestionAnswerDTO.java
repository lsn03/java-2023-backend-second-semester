package edu.java.model.stack_over_flow.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class QuestionAnswerDTO {
    private AccountDTO owner;

    @JsonProperty("is_accepted")
    private boolean isAccepted;

    @JsonProperty("creation_date")
    private OffsetDateTime creationDate;

    @JsonProperty("answer_id")
    private int answerId;

    @JsonCreator
    public QuestionAnswerDTO(
        @JsonProperty("owner") AccountDTO owner,
        @JsonProperty("is_accepted") boolean isAccepted,
        @JsonProperty("creation_date") long creationDate,
        @JsonProperty("answer_id") int answerId
    ) {
        this.owner = owner;
        this.isAccepted = isAccepted;
        this.creationDate = Instant.ofEpochSecond(creationDate).atOffset(ZoneOffset.UTC);
        this.answerId = answerId;
    }

    public void setCreationDate(OffsetDateTime time) {
        creationDate = time;
    }

    public void setCreationDate(long time) {
        creationDate = Instant.ofEpochSecond(time).atOffset(ZoneOffset.UTC);
    }
}
