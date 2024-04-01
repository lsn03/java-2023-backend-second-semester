package edu.java.domain.repository.jooq;

import edu.java.domain.jooq.tables.StackoverflowAnswer;
import edu.java.domain.jooq.tables.records.StackoverflowAnswerRecord;
import edu.java.domain.model.StackOverFlowAnswerDTO;
import edu.java.domain.repository.StackOverFlowRepository;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep7;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JooqStackOverFlowRepository implements StackOverFlowRepository {
    private final DSLContext dslContext;
    private final JooqLinkRepository jooqLinkRepository;

    @Override
    public Integer addAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList) {
        InsertValuesStep7<StackoverflowAnswerRecord,
            Long,
            Long,
            String,
            Boolean,
            LocalDateTime,
            LocalDateTime,
            LocalDateTime>

            step = dslContext.insertInto(
            StackoverflowAnswer.STACKOVERFLOW_ANSWER,
            StackoverflowAnswer.STACKOVERFLOW_ANSWER.LINK_ID,
            StackoverflowAnswer.STACKOVERFLOW_ANSWER.ANSWER_ID,
            StackoverflowAnswer.STACKOVERFLOW_ANSWER.USER_NAME,
            StackoverflowAnswer.STACKOVERFLOW_ANSWER.IS_ACCEPTED,
            StackoverflowAnswer.STACKOVERFLOW_ANSWER.CREATION_DATE,
            StackoverflowAnswer.STACKOVERFLOW_ANSWER.LAST_ACTIVITY_DATE,
            StackoverflowAnswer.STACKOVERFLOW_ANSWER.LAST_EDIT_DATE
        );

        for (var answer : stackOverFlowAnswerDTOList) {
            var last = answer.getLastEditDate();
            step.values(
                answer.getLinkId(),
                answer.getAnswerId(),
                answer.getUserName(),
                answer.getIsAccepted(),
                answer.getCreationDate().toLocalDateTime(),
                answer.getLastActivityDate().toLocalDateTime(),
                last == null ? null : last.toLocalDateTime()
            );
        }
        return step.execute();
    }

    @Override
    public Integer deleteAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList) {
        List<Long> answerIdList = stackOverFlowAnswerDTOList.stream().map(StackOverFlowAnswerDTO::getAnswerId).toList();

        return dslContext.deleteFrom(StackoverflowAnswer.STACKOVERFLOW_ANSWER)
            .where(StackoverflowAnswer.STACKOVERFLOW_ANSWER.ANSWER_ID.in(answerIdList))
            .execute();
    }

    @Override
    public List<StackOverFlowAnswerDTO> getAnswers(Long linkId) {
        return dslContext.selectFrom(StackoverflowAnswer.STACKOVERFLOW_ANSWER)
            .where(StackoverflowAnswer.STACKOVERFLOW_ANSWER.LINK_ID.eq(linkId))
            .fetch()
            .map(
                stackoverflowAnswerRecord -> {
                    StackOverFlowAnswerDTO dto = stackoverflowAnswerRecord.into(StackOverFlowAnswerDTO.class);
                    var last = stackoverflowAnswerRecord.getLastEditDate();
                    if (last != null) {
                        dto.setLastEditDate(last.atOffset(ZoneOffset.UTC));
                    }
                    dto.setCreationDate(stackoverflowAnswerRecord.getCreationDate().atOffset(ZoneOffset.UTC));
                    dto.setLastActivityDate(stackoverflowAnswerRecord.getLastActivityDate().atOffset(ZoneOffset.UTC));
                    return dto;
                }
            );
    }

    @Override
    public List<StackOverFlowAnswerDTO> getAnswers(URI uri) {
        Long id = jooqLinkRepository.findLinkIdByUrl(uri);

        return getAnswers(id);
    }
}
