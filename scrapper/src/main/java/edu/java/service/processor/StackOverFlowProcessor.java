package edu.java.service.processor;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.model.StackOverFlowAnswerDTO;
import edu.java.domain.repository.LinkRepository;
import edu.java.model.StackOverFlowQuestionUriDTO;
import edu.java.model.UriDTO;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.model.stack_over_flow.StackOverFlowModel;
import edu.java.service.client.StackOverFlowClient;
import edu.java.service.database.StackOverFlowService;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackOverFlowProcessor implements Processor {
    private static final int MAX_MESSAGE_SIZE = 5;
    private final static StringBuilder STRING_BUILDER = new StringBuilder();

    private final StackOverFlowClient stackOverFlowClient;
    private final StackOverFlowService stackOverFlowService;
    private final LinkRepository jooqLinkRepository;

    @Override
    public List<LinkUpdateRequest> processUriDTO(LinkDTO linkDTO, UriDTO uriDto) {
        if (!(uriDto instanceof StackOverFlowQuestionUriDTO)) {
            return null;
        }

        StackOverFlowQuestionUriDTO uriDto1 = (StackOverFlowQuestionUriDTO) uriDto;
        StackOverFlowModel response =
            stackOverFlowClient.fetchQuestionData(uriDto1.getQuestionId());

        List<LinkUpdateRequest> list = new ArrayList<>();
        list.add(processAnswer(linkDTO, response));
        return list;
    }

    private LinkUpdateRequest processAnswer(LinkDTO linkDTO, StackOverFlowModel response) {
        var answerFromAPI = response.getQuestionAnswerList().stream().map(
            questionAnswerDTOResponse -> {
                StackOverFlowAnswerDTO answerDTO = StackOverFlowAnswerDTO.create(questionAnswerDTOResponse);
                answerDTO.setLinkId(linkDTO.getLinkId());
                return answerDTO;
            }
        ).toList();

        if (linkDTO.getLastUpdate() == null) {
            stackOverFlowService.addAnswers(answerFromAPI);
            linkDTO.setLastUpdate(OffsetDateTime.now());
            jooqLinkRepository.updateLink(linkDTO);
            return null;
        }

        var answersFromDB = stackOverFlowService.getAnswers(linkDTO.getLinkId());
        List<StackOverFlowAnswerDTO> listForUpdate = new ArrayList<>();

        for (var answer : answerFromAPI) {
            if (!answersFromDB.contains(answer)) {
                listForUpdate.add(answer);
            }
        }

        stackOverFlowService.addAnswers(listForUpdate);

        STRING_BUILDER.setLength(0);
        if (listForUpdate.size() > MAX_MESSAGE_SIZE) {
            STRING_BUILDER.append("Появилось ").append(listForUpdate.size()).append(" ответов на сайте: ")
                .append(linkDTO.getUri()).append(System.lineSeparator());
        } else {
            for (var answer : listForUpdate) {

                STRING_BUILDER.append("Пользователь ").append(answer.getUserName()).append(" оставил ответ ")
                    .append(answer.getAnswerId()).append(System.lineSeparator());
            }
        }
        if (STRING_BUILDER.isEmpty()) {
            return null;
        }
        return new LinkUpdateRequest(
            linkDTO.getLinkId(),
            linkDTO.getUri().toString(),
            STRING_BUILDER.toString(),
            null
        );
    }
}
