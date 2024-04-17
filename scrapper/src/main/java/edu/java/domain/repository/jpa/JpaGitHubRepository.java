package edu.java.domain.repository.jpa;

import edu.java.domain.model.GitHubCommitDTO;
import edu.java.domain.repository.GitHubRepository;
import edu.java.domain.repository.jpa.mapper.MapperGitHubCommitDTOGitHubCommitEntity;
import edu.java.exception.exception.RecordAlreadyExistException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;

@RequiredArgsConstructor
public class JpaGitHubRepository implements GitHubRepository {
    private final EntityManager entityManager;
    private final JpaGitHubRepositoryInterface jpaGitHubRepository;

    @Override
    public Integer addCommits(List<GitHubCommitDTO> gitHubCommitList) {
        int cnt = 0;
        for (var commitDto : gitHubCommitList) {
            var entity = MapperGitHubCommitDTOGitHubCommitEntity.dtoToEntity(commitDto);
            try {
                entityManager.persist(entity);
            } catch (EntityExistsException e) {
                throw new RecordAlreadyExistException(e);
            } catch (ConstraintViolationException e) {
                if (e.getMessage().contains("duplicate key value ")) {
                    throw new RecordAlreadyExistException(e);
                } else {
                    throw new RuntimeException(e);
                }
            }
            cnt++;
        }

        return cnt;
    }

    @Override
    public Integer deleteCommits(List<GitHubCommitDTO> gitHubCommitList) {
        int cnt = 0;
        for (var commit : gitHubCommitList) {
            var entity = jpaGitHubRepository.findByLinkEntityLinkIdAndSha(commit.getLinkId(), commit.getSha());

            entityManager.remove(entity);

            cnt++;
        }

        return cnt;
    }

    @Override
    public List<GitHubCommitDTO> getCommits(Long linkId) {
        return jpaGitHubRepository.findAllByLinkEntityLinkId(linkId).stream()
            .map(MapperGitHubCommitDTOGitHubCommitEntity::entityToDto).toList();
    }

    @Override
    public List<GitHubCommitDTO> getCommits(URI uri) {
        return jpaGitHubRepository.findAllByLinkEntityUri(uri.toString()).stream()
            .map(MapperGitHubCommitDTOGitHubCommitEntity::entityToDto).toList();
    }
}
