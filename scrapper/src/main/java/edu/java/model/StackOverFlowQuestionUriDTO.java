package edu.java.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StackOverFlowQuestionUriDTO extends UriDTO {
    private Integer questionId;
}
