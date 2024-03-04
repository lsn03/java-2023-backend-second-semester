package edu.java.model.stack_over_flow.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.util.Utils;
import java.time.OffsetDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class QuestionAnswerDTOResponse {
    private AccountDTO owner;

    @JsonProperty("is_accepted")
    private boolean isAccepted;

    @JsonProperty("creation_date")
    private OffsetDateTime creationDate;

    @JsonProperty("last_activity_date")
    private OffsetDateTime lastActivityDate;

    @JsonProperty("last_edit_date")
    private OffsetDateTime lastEditDate;

    @JsonProperty("answer_id")
    private int answerId;

    @JsonCreator
    public QuestionAnswerDTOResponse(
        @JsonProperty("owner") AccountDTO owner,
        @JsonProperty("is_accepted") boolean isAccepted,
        @JsonProperty("creation_date") Long creationDate,
        @JsonProperty("last_activity_date") Long lastActivityDate,
        @JsonProperty("last_edit_date") Long lastEditDate,
        @JsonProperty("answer_id") int answerId
    ) {
        this.owner = owner;
        this.isAccepted = isAccepted;
        this.creationDate = Utils.convertLongToOffsetDayTime(creationDate);
        this.lastEditDate = Utils.convertLongToOffsetDayTime(lastEditDate);
        this.lastActivityDate = lastActivityDate == null ? null : Utils.convertLongToOffsetDayTime(lastActivityDate);
        this.answerId = answerId;

    }

}
