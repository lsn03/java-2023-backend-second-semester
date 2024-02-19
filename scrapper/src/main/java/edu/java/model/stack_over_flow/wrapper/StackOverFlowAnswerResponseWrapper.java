package edu.java.model.stack_over_flow.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.model.stack_over_flow.dto.QuestionAnswerDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StackOverFlowAnswerResponseWrapper {
    @JsonProperty("items")
    private List<QuestionAnswerDTO> questionAnswerDTOList;
}
