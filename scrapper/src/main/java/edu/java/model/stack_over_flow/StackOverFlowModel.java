package edu.java.model.stack_over_flow;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.model.stack_over_flow.dto.QuestionAnswerDTOResponse;
import edu.java.model.stack_over_flow.dto.QuestionHeaderDTOResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StackOverFlowModel {
    @JsonProperty("question_header")
    private QuestionHeaderDTOResponse questionHeader;
    @JsonProperty("question_answer_list")
    private List<QuestionAnswerDTOResponse> questionAnswerList;

}
