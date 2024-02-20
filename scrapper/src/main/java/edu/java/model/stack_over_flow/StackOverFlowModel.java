package edu.java.model.stack_over_flow;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.model.stack_over_flow.dto.QuestionAnswerDTO;
import edu.java.model.stack_over_flow.dto.QuestionHeaderDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class StackOverFlowModel {
    @JsonProperty("question_header")
    private QuestionHeaderDTO questionHeader;
    @JsonProperty("question_answer_list")
    private List<QuestionAnswerDTO> questionAnswerList;

}
