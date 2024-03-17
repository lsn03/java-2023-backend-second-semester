package edu.java.domain.repository.jooq;

import edu.java.domain.jooq.tables.GithubCommit;
import edu.java.domain.jooq.tables.records.GithubCommitRecord;
import edu.java.domain.model.GitHubCommitDTO;
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
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JooqGitHubRepository implements GitHubRepository {
    private final DSLContext dslContext;
    private final JooqLinkRepository jooqLinkRepository;

    @Override
    @Transactional
    public Integer addCommits(List<GitHubCommitDTO> gitHubCommitList) {
        InsertValuesStep5<GithubCommitRecord, Long, String, String, LocalDateTime, String> step = dslContext
            .insertInto(
                GithubCommit.GITHUB_COMMIT,
                GithubCommit.GITHUB_COMMIT.LINK_ID,
                GithubCommit.GITHUB_COMMIT.SHA,
                GithubCommit.GITHUB_COMMIT.AUTHOR,
                GithubCommit.GITHUB_COMMIT.CREATED_AT,
                GithubCommit.GITHUB_COMMIT.MESSAGE
            );

        for (GitHubCommitDTO commit : gitHubCommitList) {
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
    @Transactional
    public Integer deleteCommits(List<GitHubCommitDTO> gitHubCommitList) {

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
    @Transactional
    public List<GitHubCommitDTO> getCommits(Long linkId) {
        return dslContext.selectFrom(GithubCommit.GITHUB_COMMIT)
            .where(GithubCommit.GITHUB_COMMIT.LINK_ID.eq(linkId))
            .fetch()
            .map(githubCommitRecord -> {
                GitHubCommitDTO dto = githubCommitRecord.into(GitHubCommitDTO.class);
                dto.setCreatedAt(githubCommitRecord.getCreatedAt().atOffset(ZoneOffset.UTC));
                return dto;
            });
    }

    @Override
    @Transactional
    public List<GitHubCommitDTO> getCommits(URI uri) {
        Long id = jooqLinkRepository.findLinkIdByUrl(uri);
        return getCommits(id);
    }

}
