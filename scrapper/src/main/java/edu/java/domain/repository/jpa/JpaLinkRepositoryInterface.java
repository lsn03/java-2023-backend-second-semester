package edu.java.domain.repository.jpa;

import edu.java.domain.repository.jpa.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public interface JpaLinkRepositoryInterface extends JpaRepository<LinkEntity, Long> {
    Optional<LinkEntity> findLinkEntityByUri(String uri);
    Optional<LinkEntity> findLinkEntityByLinkId(Long linkId);
}
