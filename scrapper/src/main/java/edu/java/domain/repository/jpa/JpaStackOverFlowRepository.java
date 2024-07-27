package edu.java.domain.repository.jpa;

import edu.java.domain.model.StackOverFlowAnswerDto;
import edu.java.domain.repository.StackOverFlowRepository;
import edu.java.domain.repository.jpa.mapper.MapperStackOverFlowDTOEntity;
import edu.java.exception.exception.RecordAlreadyExistException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;

@RequiredArgsConstructor
public class JpaStackOverFlowRepository implements StackOverFlowRepository {
    private final EntityManager entityManager;
    private final JpaStackOverFlowRepositoryInterface jpaStackOverFlowRepository;

    @Override
    public Integer addAnswers(List<StackOverFlowAnswerDto> stackOverFlowAnswerDTOList) {
        int cnt = 0;
        for (var dto : stackOverFlowAnswerDTOList) {
            var entity = MapperStackOverFlowDTOEntity.dtoToEntity(dto);
            try {
                entityManager.persist(entity);
            } catch (EntityExistsException e) {
                throw new RecordAlreadyExistException(e);
            } catch (ConstraintViolationException e) {
                if (e.getMessage().contains("duplicate key value")) {
                    throw new RecordAlreadyExistException(e);
                }
            }
            cnt++;
        }

        return cnt;
    }

    @Override
    public Integer deleteAnswers(List<StackOverFlowAnswerDto> stackOverFlowAnswerDTOList) {
        int cnt = 0;
        for (var dto : stackOverFlowAnswerDTOList) {
            var entity = jpaStackOverFlowRepository.findByAnswerId(dto.getAnswerId());
            entityManager.remove(entity);
            cnt++;
        }

        return cnt;
    }

    @Override
    public List<StackOverFlowAnswerDto> getAnswers(Long linkId) {

        return jpaStackOverFlowRepository.findAllByLinkEntityLinkId(linkId).stream()
            .map(MapperStackOverFlowDTOEntity::entityToDto).toList();
    }

    @Override
    public List<StackOverFlowAnswerDto> getAnswers(URI uri) {
        return jpaStackOverFlowRepository.findAllByLinkEntityUri(uri.toString()).stream()
            .map(MapperStackOverFlowDTOEntity::entityToDto).toList();
    }
}
