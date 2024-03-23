package edu.java.domain.repository.jpa;

import edu.java.domain.model.StackOverFlowAnswerDTO;
import edu.java.domain.repository.StackOverFlowRepository;
import edu.java.domain.repository.jpa.mapper.MapperStackOverFlowDTOEntity;
import jakarta.persistence.EntityManager;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class JpaStackOverFlowRepository implements StackOverFlowRepository {
    private final EntityManager entityManager;
    private final JpaStackOverFlowRepositoryInterface jpaStackOverFlowRepository;

    @Override
    @Transactional
    public Integer addAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList) {
        int cnt = 0;
        for (var dto : stackOverFlowAnswerDTOList) {
            var entity = MapperStackOverFlowDTOEntity.dtoToEntity(dto);
            entityManager.persist(entity);
            cnt++;
        }
        return cnt;
    }

    @Override
    @Transactional
    public Integer deleteAnswers(List<StackOverFlowAnswerDTO> stackOverFlowAnswerDTOList) {
        int cnt = 0;
        for (var dto : stackOverFlowAnswerDTOList) {
            var entity = MapperStackOverFlowDTOEntity.dtoToEntity(dto);
            entityManager.remove(entity);
            cnt++;
        }
        return cnt;
    }

    @Override
    @Transactional
    public List<StackOverFlowAnswerDTO> getAnswers(Long linkId) {

        return jpaStackOverFlowRepository.findAllByLinkEntityLinkId(linkId).stream()
            .map(MapperStackOverFlowDTOEntity::entityToDto).toList();
    }

    @Override
    @Transactional
    public List<StackOverFlowAnswerDTO> getAnswers(URI uri) {
        return jpaStackOverFlowRepository.findAllByLinkEntityUri(uri.toString()).stream()
            .map(MapperStackOverFlowDTOEntity::entityToDto).toList();
    }
}
