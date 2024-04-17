package edu.java.scrapper.hw5bonus.processor;

import edu.java.domain.model.LinkDto;
import edu.java.domain.model.StackOverFlowAnswerDto;
import edu.java.domain.repository.LinkRepository;
import edu.java.model.StackOverFlowQuestionUriDto;
import edu.java.model.stack_over_flow.StackOverFlowModel;
import edu.java.model.stack_over_flow.dto.AccountDto;
import edu.java.model.stack_over_flow.dto.QuestionAnswerDtoResponse;
import edu.java.service.client.StackOverFlowClient;
import edu.java.service.database.StackOverFlowService;
import edu.java.service.processor.StackOverFlowProcessor;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StackOverFlowProcessorTest {
    @Mock
    StackOverFlowClient stackOverFlowClient;
    @Mock
    LinkRepository linkRepository;
    @Mock
    StackOverFlowService stackOverFlowService;
    OffsetDateTime time = OffsetDateTime.of(2015, 1, 1, 1, 1, 1, 0, ZoneOffset.UTC);
    LinkDto linkDTO;
    StackOverFlowModel dataFromApi;

    @Test
    public void testUriProcessor() {
        dataFromApi = new StackOverFlowModel();
        List<QuestionAnswerDtoResponse> listFromApi = List.of(
            new QuestionAnswerDtoResponse(new AccountDto(1, "name"), false, 1l, 1l, null, 1),
            new QuestionAnswerDtoResponse(new AccountDto(2, "nam2"), false, 2l, 2l, null, 2)

        );
        List<StackOverFlowAnswerDto> listFromDb = List.of(
            StackOverFlowAnswerDto.create(listFromApi.getFirst())
        );

        when(stackOverFlowClient.fetchQuestionData(1)).thenReturn(dataFromApi);
        when(stackOverFlowService.addAnswers(anyList())).thenReturn(1);
        when(stackOverFlowService.getAnswers(anyLong())).thenReturn(listFromDb);
        lenient().doNothing().when(linkRepository).updateLink(any(LinkDto.class));

        dataFromApi.setQuestionAnswerList(listFromApi);

        linkDTO = new LinkDto();
        linkDTO.setLastUpdate(time);
        linkDTO.setLinkId(1l);
        linkDTO.setUri(URI.create("https://github.com/lsn03/java-2023-backend-second-semester/pull/5"));
        linkDTO.setCreatedAt(time);

        StackOverFlowProcessor stackOverFlowProcessor =
            new StackOverFlowProcessor(stackOverFlowClient, stackOverFlowService, linkRepository);
        var responseList = stackOverFlowProcessor.processUriDTO(linkDTO, new StackOverFlowQuestionUriDto(1));

        assertTrue(responseList.getFirst().getDescription().contains("Пользователь"));

    }

}
