package edu.java.domain.repository.jooq;

import edu.java.domain.jooq.tables.GithubCommit;
import edu.java.domain.jooq.tables.records.GithubCommitRecord;
import edu.java.domain.model.GitHubCommitDto;
import edu.java.domain.repository.GitHubRepository;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep5;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JooqGitHubRepository implements GitHubRepository {
    private final DSLContext dslContext;
    private final JooqLinkRepository jooqLinkRepository;

    @Override
    public Integer addCommits(List<GitHubCommitDto> gitHubCommitList) {
        InsertValuesStep5<GithubCommitRecord, Long, String, String, LocalDateTime, String> step = dslContext
            .insertInto(
                GithubCommit.GITHUB_COMMIT,
                GithubCommit.GITHUB_COMMIT.LINK_ID,
                GithubCommit.GITHUB_COMMIT.SHA,
                GithubCommit.GITHUB_COMMIT.AUTHOR,
                GithubCommit.GITHUB_COMMIT.CREATED_AT,
                GithubCommit.GITHUB_COMMIT.MESSAGE
            );

        for (GitHubCommitDto commit : gitHubCommitList) {
            step.values(
                commit.getLinkId(),
                commit.getSha(),
                commit.getAuthor(),
                commit.getCreatedAt().toLocalDateTime(),
                commit.getMessage()
            );
        }

        return step.execute();
    }

    @Override
    public Integer deleteCommits(List<GitHubCommitDto> gitHubCommitList) {

        Condition condition = DSL.noCondition();
        for (var commit : gitHubCommitList) {
            Condition currentCondition = GithubCommit.GITHUB_COMMIT.LINK_ID.eq(commit.getLinkId())
                .and(GithubCommit.GITHUB_COMMIT.SHA.eq(commit.getSha()));
            condition.or(currentCondition);
        }
        return dslContext.deleteFrom(GithubCommit.GITHUB_COMMIT)
            .where(condition)
            .execute();
    }

    @Override
    public List<GitHubCommitDto> getCommits(Long linkId) {
        return dslContext.selectFrom(GithubCommit.GITHUB_COMMIT)
            .where(GithubCommit.GITHUB_COMMIT.LINK_ID.eq(linkId))
            .fetch()
            .map(githubCommitRecord -> {
                GitHubCommitDto dto = githubCommitRecord.into(GitHubCommitDto.class);
                dto.setCreatedAt(githubCommitRecord.getCreatedAt().atOffset(ZoneOffset.UTC));
                return dto;
            });
    }

    @Override
    public List<GitHubCommitDto> getCommits(URI uri) {
        Long id = jooqLinkRepository.findLinkIdByUrl(uri);
        return getCommits(id);
    }

}
