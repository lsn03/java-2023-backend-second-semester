package edu.java.domain.repository.jpa.mapper;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.jpa.entity.LinkEntity;
import java.net.URI;
import java.time.ZoneOffset;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapperLinkDTOLinkEntity {
    public static LinkDTO linkEntityToLinkDTO(LinkEntity linkEntity) {
        var created = linkEntity.getCreatedAt();
        var update = linkEntity.getLastUpdate();

        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setUri(URI.create(linkEntity.getUri()));
        linkDTO.setLinkId(linkEntity.getLinkId());
        if (created != null) {
            linkDTO.setCreatedAt(created.atOffset(ZoneOffset.UTC));
        }
        if (update != null) {
            linkDTO.setLastUpdate(update.atOffset(ZoneOffset.UTC));
        }

        return linkDTO;
    }

    public static LinkEntity linkDTOToLinkEntity(LinkDTO linkDTO) {
        var created = linkDTO.getCreatedAt();
        var update = linkDTO.getLastUpdate();

        LinkEntity linkEntity = new LinkEntity();
        linkEntity.setUri(linkDTO.getUri().toString());
        linkEntity.setLinkId(linkDTO.getLinkId());

        if (created != null) {
            linkEntity.setCreatedAt(created.toLocalDateTime());
        }
        if (update != null) {
            linkEntity.setLastUpdate(update.toLocalDateTime());
        }

        return linkEntity;
    }
}
