package edu.java.service.processor;

import edu.java.domain.model.LinkDto;
import edu.java.domain.model.StackOverFlowAnswerDto;
import edu.java.domain.repository.LinkRepository;
import edu.java.model.StackOverFlowQuestionUriDto;
import edu.java.model.UriDto;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.model.stack_over_flow.StackOverFlowModel;
import edu.java.service.client.StackOverFlowClient;
import edu.java.service.database.StackOverFlowService;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StackOverFlowProcessor implements Processor {
    private static final int MAX_MESSAGE_SIZE = 5;
    private final static StringBuilder STRING_BUILDER = new StringBuilder();

    private final StackOverFlowClient stackOverFlowClient;
    private final StackOverFlowService stackOverFlowService;
    private final LinkRepository jooqLinkRepository;

    @Override
    public List<LinkUpdateRequest> processUriDTO(LinkDto linkDTO, UriDto uriDto) {
        if (!(uriDto instanceof StackOverFlowQuestionUriDto)) {
            return null;
        }

        StackOverFlowQuestionUriDto uriDto1 = (StackOverFlowQuestionUriDto) uriDto;
        StackOverFlowModel response =
            stackOverFlowClient.fetchQuestionData(uriDto1.getQuestionId());

        List<LinkUpdateRequest> list = new ArrayList<>();
        list.add(processAnswer(linkDTO, response));
        return list;
    }

    private LinkUpdateRequest processAnswer(LinkDto linkDTO, StackOverFlowModel response) {
        var answerFromAPI = response.getQuestionAnswerList().stream().map(
            questionAnswerDTOResponse -> {
                StackOverFlowAnswerDto answerDTO = StackOverFlowAnswerDto.create(questionAnswerDTOResponse);
                answerDTO.setLinkId(linkDTO.getLinkId());
                return answerDTO;
            }
        ).toList();

        if (linkDTO.getLastUpdate() == null) {
            stackOverFlowService.addAnswers(answerFromAPI);
            linkDTO.setLastUpdate(OffsetDateTime.now());
            jooqLinkRepository.updateLink(linkDTO);
            log.info("last_update is null, add {} answers for link_id {}", answerFromAPI.size(), linkDTO.getLinkId());
            return null;
        }

        var answersFromDB = stackOverFlowService.getAnswers(linkDTO.getLinkId());
        List<StackOverFlowAnswerDto> listForUpdate = new ArrayList<>();

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
        log.info("generate LinkUpdateRequest for link_id {}", linkDTO.getLinkId());
        return new LinkUpdateRequest(
            linkDTO.getLinkId(),
            linkDTO.getUri().toString(),
            STRING_BUILDER.toString(),
            null
        );
    }
}
