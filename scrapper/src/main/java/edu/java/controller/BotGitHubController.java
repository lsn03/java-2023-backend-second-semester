package edu.java.controller;

import edu.java.model.stack_over_flow.StackOverFlowModel;
import edu.java.model.stack_over_flow.dto.QuestionAnswerDTO;
import edu.java.model.stack_over_flow.dto.QuestionHeaderDTO;
import edu.java.service.client.StackOverFlowClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/scrapper/sof")
@Slf4j
public class BotGitHubController {

    private StackOverFlowClient service;

    @GetMapping("/1")
    public List<QuestionAnswerDTO> trackGitHubUrl() {
        return service.fetchAnswers(6402162).block();

    }
    @GetMapping("/2")
    public QuestionHeaderDTO trackGitHubUrl2() {
        return service.fetchHeader(6402162).block();

    }
    @GetMapping("/3")
    public StackOverFlowModel trackGitHubUrl3() {
        return service.fetchQuestionData(6402162).block();

    }
}
