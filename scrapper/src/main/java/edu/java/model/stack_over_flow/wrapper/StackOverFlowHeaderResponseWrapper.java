package edu.java.model.stack_over_flow.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.model.stack_over_flow.dto.QuestionHeaderDTOResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StackOverFlowHeaderResponseWrapper {
    @JsonProperty("items")
    private List<QuestionHeaderDTOResponse> list;
}
