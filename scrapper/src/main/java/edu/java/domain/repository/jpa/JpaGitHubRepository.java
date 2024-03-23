package edu.java.domain.repository.jpa;

import edu.java.domain.model.GitHubCommitDTO;
import edu.java.domain.repository.GitHubRepository;
import edu.java.domain.repository.jpa.mapper.MapperGitHubCommitDTOGitHubCommitEntity;
import jakarta.persistence.EntityManager;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class JpaGitHubRepository implements GitHubRepository {
    private final EntityManager entityManager;
    private final JpaGitHubRepositoryInterface jpaGitHubRepository;

    @Override
    @Transactional
    public Integer addCommits(List<GitHubCommitDTO> gitHubCommitList) {
        int cnt = 0;
        for (var commitDto : gitHubCommitList) {
            var entity = MapperGitHubCommitDTOGitHubCommitEntity.dtoToEntity(commitDto);
            entityManager.persist(entity);
            cnt++;
        }
        return cnt;
    }

    @Override
    @Transactional
    public Integer deleteCommits(List<GitHubCommitDTO> gitHubCommitList) {
        int cnt = 0;
        for (var commit : gitHubCommitList) {
            var entity = MapperGitHubCommitDTOGitHubCommitEntity.dtoToEntity(commit);
            entityManager.remove(entity);
            cnt++;
        }
        return cnt;
    }

    @Override
    @Transactional
    public List<GitHubCommitDTO> getCommits(Long linkId) {
        return jpaGitHubRepository.findAllByLinkEntity_LinkId(linkId).stream()
            .map(MapperGitHubCommitDTOGitHubCommitEntity::entityToDto).toList();
    }

    @Override
    @Transactional
    public List<GitHubCommitDTO> getCommits(URI uri) {
        return jpaGitHubRepository.findAllByLinkEntityUri(uri.toString()).stream()
            .map(MapperGitHubCommitDTOGitHubCommitEntity::entityToDto).toList();
    }
}
