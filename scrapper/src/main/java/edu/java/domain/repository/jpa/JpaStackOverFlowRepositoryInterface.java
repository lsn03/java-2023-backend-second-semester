package edu.java.domain.repository.jpa;

import edu.java.domain.repository.jpa.entity.StackOverFlowAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaStackOverFlowRepositoryInterface extends JpaRepository<StackOverFlowAnswerEntity, Long> {
    List<StackOverFlowAnswerEntity> findAllByLinkEntityLinkId(Long linkId);
    List<StackOverFlowAnswerEntity> findAllByLinkEntityUri(String uri);
}
