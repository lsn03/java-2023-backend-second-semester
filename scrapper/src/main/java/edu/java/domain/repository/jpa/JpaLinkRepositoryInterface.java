package edu.java.domain.repository.jpa;

import edu.java.domain.repository.jpa.entity.LinkEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLinkRepositoryInterface extends JpaRepository<LinkEntity, Long> {
    Optional<LinkEntity> findLinkEntityByUri(String uri);

    Optional<LinkEntity> findLinkEntityByLinkId(Long linkId);
}
