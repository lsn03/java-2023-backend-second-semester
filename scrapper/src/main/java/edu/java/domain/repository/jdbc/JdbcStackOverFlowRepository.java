package edu.java.domain.repository.jdbc;

import edu.java.domain.model.StackOverFlowAnswerDTO;
import edu.java.domain.repository.StackOverFlowRepository;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JdbcStackOverFlowRepository implements StackOverFlowRepository {
    private static final int INS_LINK_ID_INDEX = 1;
    private static final int INS_ANSWER_ID_INDEX = 2;
    private static final int INS_USERNAME_INDEX = 3;
    private static final int INS_IS_ACCEPTED_INDEX = 4;
    private static final int INS_CREATION_DATE_INDEX = 5;
    private static final int INS_LAST_ACTIVITY_DATE_INDEX = 6;
    private static final int INS_LAST_EDIT_INDEX = 7;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer addAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList) {
        String sql =
            "insert into stackoverflow_answer (link_id, answer_id, user_name, is_accepted, creation_date, "
                + "last_activity_date, last_edit_date)"
                + "values (?,?,?,?,?,?,?);";
        int[][] updateCounts = jdbcTemplate.batchUpdate(
            sql,
            stackOverFlowAnswerDTOList,
            stackOverFlowAnswerDTOList.size(),
            (ps, answer) -> {
                var last = answer.getLastEditDate();

                ps.setLong(INS_LINK_ID_INDEX, answer.getLinkId());
                ps.setLong(INS_ANSWER_ID_INDEX, answer.getAnswerId());
                ps.setString(INS_USERNAME_INDEX, answer.getUserName());
                ps.setBoolean(INS_IS_ACCEPTED_INDEX, answer.getIsAccepted());
                ps.setObject(INS_CREATION_DATE_INDEX, answer.getCreationDate().toLocalDateTime());
                ps.setObject(INS_LAST_ACTIVITY_DATE_INDEX, answer.getLastActivityDate().toLocalDateTime());
                ps.setObject(INS_LAST_EDIT_INDEX, last == null ? null : last.toLocalDateTime());

            }
        );
        return getSum(updateCounts);
    }

    @Override
    public Integer deleteAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList) {
        List<Long> answerIdList = stackOverFlowAnswerDTOList.stream()
            .map(StackOverFlowAnswerDTO::getAnswerId)
            .toList();

        String inSql = String.join(",", Collections.nCopies(answerIdList.size(), "?"));

        String sql = "DELETE FROM stackoverflow_answer WHERE answer_id IN (" + inSql + ")";

        Object[] args = answerIdList.toArray();

        return jdbcTemplate.update(sql, args);

    }

    @Override
    public List<StackOverFlowAnswerDTO> getAnswers(Long linkId) {
        return jdbcTemplate.query(
            """
                select link_id, answer_id, user_name, is_accepted, creation_date, last_activity_date, last_edit_date
                from stackoverflow_answer
                where link_id = ?;
                                """, (rs, rowNum) -> {
                return getStackOverFlowAnswerDTO(rs);
            }, new Object[] {linkId}
        );
    }

    @Override
    public List<StackOverFlowAnswerDTO> getAnswers(URI uri) {
        return jdbcTemplate.query(
            """
                select l.link_id, answer_id, user_name, is_accepted, creation_date, last_activity_date, last_edit_date
                from stackoverflow_answer
                inner join public.link l on l.link_id = stackoverflow_answer.link_id
                where l.uri = ?
                """, (rs, rowNum) -> {
                return getStackOverFlowAnswerDTO(rs);
            }, new Object[] {uri.toString()}
        );
    }

    private static int getSum(int[][] updateCounts) {
        return Arrays.stream(updateCounts).flatMapToInt(Arrays::stream).sum();
    }

    @NotNull
    private static StackOverFlowAnswerDTO getStackOverFlowAnswerDTO(ResultSet rs) throws SQLException {
        var last = rs.getTimestamp(INS_LAST_EDIT_INDEX);
        return new StackOverFlowAnswerDTO(
            rs.getLong(INS_LINK_ID_INDEX),
            rs.getLong(INS_ANSWER_ID_INDEX),
            rs.getString(INS_USERNAME_INDEX),
            rs.getBoolean(INS_IS_ACCEPTED_INDEX),
            rs.getTimestamp(INS_CREATION_DATE_INDEX).toLocalDateTime().atOffset(ZoneOffset.UTC),
            rs.getTimestamp(INS_LAST_ACTIVITY_DATE_INDEX).toLocalDateTime().atOffset(ZoneOffset.UTC),
            last == null ? null : last.toLocalDateTime().atOffset(ZoneOffset.UTC)

        );
    }
}
