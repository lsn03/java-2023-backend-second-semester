package edu.java.domain.repository.jpa;

import edu.java.domain.repository.jpa.entity.StackOverFlowAnswerEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStackOverFlowRepositoryInterface extends JpaRepository<StackOverFlowAnswerEntity, Long> {
    List<StackOverFlowAnswerEntity> findAllByLinkEntityLinkId(Long linkId);

    StackOverFlowAnswerEntity findByAnswerId(Long answerId);

    List<StackOverFlowAnswerEntity> findAllByLinkEntityUri(String uri);
}
