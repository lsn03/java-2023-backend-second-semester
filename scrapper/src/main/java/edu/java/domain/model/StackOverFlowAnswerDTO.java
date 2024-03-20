package edu.java.domain.model;

import edu.java.model.stack_over_flow.dto.QuestionAnswerDTOResponse;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StackOverFlowAnswerDTO {
    private Long linkId;
    private Long answerId;
    private String userName;
    private Boolean isAccepted;
    private OffsetDateTime creationDate;
    private OffsetDateTime lastActivityDate;
    private OffsetDateTime lastEditDate;

    public static StackOverFlowAnswerDTO create(QuestionAnswerDTOResponse questionAnswerDTOResponse) {
        StackOverFlowAnswerDTO stackOverFlowAnswerDTO = new StackOverFlowAnswerDTO();

        stackOverFlowAnswerDTO.setAnswerId((long) questionAnswerDTOResponse.getAnswerId());
        stackOverFlowAnswerDTO.setUserName(questionAnswerDTOResponse.getOwner().getName());
        stackOverFlowAnswerDTO.setIsAccepted(questionAnswerDTOResponse.isAccepted());
        stackOverFlowAnswerDTO.setCreationDate(questionAnswerDTOResponse.getCreationDate()
            .withOffsetSameInstant(ZoneOffset.UTC));
        stackOverFlowAnswerDTO.setLastActivityDate(questionAnswerDTOResponse.getLastActivityDate()
            .withOffsetSameInstant(ZoneOffset.UTC));
        var lastEdit = questionAnswerDTOResponse.getLastEditDate();
        if (lastEdit != null) {
            stackOverFlowAnswerDTO.setLastEditDate(lastEdit.withOffsetSameInstant(
                ZoneOffset.UTC));
        }

        return stackOverFlowAnswerDTO;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StackOverFlowAnswerDTO answerDTO = (StackOverFlowAnswerDTO) o;
        return Objects.equals(linkId, answerDTO.linkId) && Objects.equals(answerId, answerDTO.answerId)
            && Objects.equals(userName, answerDTO.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(linkId, answerId, userName);
    }
}
