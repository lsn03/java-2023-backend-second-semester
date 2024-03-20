package edu.java.scrapper.hw5bonus.processor;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.model.StackOverFlowAnswerDTO;
import edu.java.domain.repository.LinkRepository;
import edu.java.model.StackOverFlowQuestionUriDTO;
import edu.java.model.stack_over_flow.StackOverFlowModel;
import edu.java.model.stack_over_flow.dto.AccountDTO;
import edu.java.model.stack_over_flow.dto.QuestionAnswerDTOResponse;
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
    LinkDTO linkDTO;
    StackOverFlowModel dataFromApi;

    @Test
    public void testUriProcessor() {
        dataFromApi = new StackOverFlowModel();
        List<QuestionAnswerDTOResponse> listFromApi = List.of(
            new QuestionAnswerDTOResponse(new AccountDTO(1, "name"), false, 1l, 1l, null, 1),
            new QuestionAnswerDTOResponse(new AccountDTO(2, "nam2"), false, 2l, 2l, null, 2)

        );
        List<StackOverFlowAnswerDTO> listFromDb = List.of(
            StackOverFlowAnswerDTO.create(listFromApi.getFirst())
        );

        when(stackOverFlowClient.fetchQuestionData(1)).thenReturn(dataFromApi);
        when(stackOverFlowService.addAnswers(anyList())).thenReturn(1);
        when(stackOverFlowService.getAnswers(anyLong())).thenReturn(listFromDb);
        lenient().doNothing().when(linkRepository).updateLink(any(LinkDTO.class));

        dataFromApi.setQuestionAnswerList(listFromApi);

        linkDTO = new LinkDTO();
        linkDTO.setLastUpdate(time);
        linkDTO.setLinkId(1l);
        linkDTO.setUri(URI.create("https://github.com/lsn03/java-2023-backend-second-semester/pull/5"));
        linkDTO.setCreatedAt(time);

        StackOverFlowProcessor stackOverFlowProcessor =
            new StackOverFlowProcessor(stackOverFlowClient, stackOverFlowService, linkRepository);
        var responseList = stackOverFlowProcessor.processUriDTO(linkDTO, new StackOverFlowQuestionUriDTO(1));

        assertTrue(responseList.getFirst().getDescription().contains("Пользователь"));

    }

}
