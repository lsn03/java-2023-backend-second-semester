package edu.java.domain.repository.jdbc;

import edu.java.domain.model.GitHubCommitDTO;
import edu.java.domain.repository.GitHubRepository;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class JdbcGitHubRepository implements GitHubRepository {
    private static final int INS_INDEX_LINK_ID = 1;
    private static final int INS_INDEX_SHA = 2;
    private static final int INS_INDEX_AUTHOR = 3;
    private static final int INS_INDEX_CREATED_AT = 4;
    private static final int INS_INDEX_MESSAGE = 5;

    private static final int SEL_INDEX_COMMIT_ID = 1;
    private static final int SEL_INDEX_LINK_ID = 2;
    private static final int SEL_INDEX_SHA = 3;
    private static final int SEL_INDEX_AUTHOR = 4;
    private static final int SEL_INDEX_CREATED_AT = 5;
    private static final int SEL_INDEX_MESSAGE = 6;
    private static final String GET_COMMITS_BY_URI = """
        select  commit_id, github_commit.link_id, sha, author, github_commit.created_at, message
        from github_commit  inner join public.link l on l.link_id = github_commit.link_id
        where l.uri = ?
        """;
    private static final String GET_COMMITS_BY_LINK_ID =
        "select commit_id, link_id, sha, author, created_at, message from github_commit where link_id = ?";
    private static final String DELETE_BY_LINK_ID_SHA = "delete from github_commit where link_id = ? and sha = ?";
    private static final String ADD_COMMIT =
        "insert into github_commit (link_id, sha, author, created_at, message) values (?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer addCommits(List<GitHubCommitDTO> gitHubCommitList) {

        int[][] updateCounts = jdbcTemplate.batchUpdate(ADD_COMMIT, gitHubCommitList, gitHubCommitList.size(),
            (ps, commit) -> {
                ps.setLong(INS_INDEX_LINK_ID, commit.getLinkId());
                ps.setString(INS_INDEX_SHA, commit.getSha());
                ps.setString(INS_INDEX_AUTHOR, commit.getAuthor());
                ps.setObject(INS_INDEX_CREATED_AT, commit.getCreatedAt().toLocalDateTime());
                ps.setString(INS_INDEX_MESSAGE, commit.getMessage());
            }
        );

        return getSum(updateCounts);
    }

    @Override
    public Integer deleteCommits(List<GitHubCommitDTO> gitHubCommitList) {

        int[][] updateCounts =
            jdbcTemplate.batchUpdate(DELETE_BY_LINK_ID_SHA, gitHubCommitList, gitHubCommitList.size(),
                (ps, commit) -> {
                    ps.setLong(1, commit.getLinkId());
                    ps.setString(2, commit.getSha());
                }
            );

        return getSum(updateCounts);
    }

    @Override
    public List<GitHubCommitDTO> getCommits(Long linkId) {
        return jdbcTemplate.query(
            GET_COMMITS_BY_LINK_ID,
            (rs, rowNum) -> getGitHubCommitDTO(rs), linkId
        );
    }

    @Override
    public List<GitHubCommitDTO> getCommits(URI uri) {

        return jdbcTemplate.query(
            GET_COMMITS_BY_URI,
            (rs, rowNum) -> {
                return getGitHubCommitDTO(rs);
            },
            uri.toString()
        );
    }

    @NotNull
    private static GitHubCommitDTO getGitHubCommitDTO(ResultSet rs) throws SQLException {
        return new GitHubCommitDTO(
            rs.getLong(SEL_INDEX_COMMIT_ID),
            rs.getLong(SEL_INDEX_LINK_ID),
            rs.getString(SEL_INDEX_SHA),
            rs.getString(SEL_INDEX_AUTHOR),
            rs.getTimestamp(SEL_INDEX_CREATED_AT).toLocalDateTime().atOffset(ZoneOffset.UTC),
            rs.getString(SEL_INDEX_MESSAGE)
        );
    }

    private static int getSum(int[][] updateCounts) {
        return Arrays.stream(updateCounts).flatMapToInt(Arrays::stream).sum();
    }
}
