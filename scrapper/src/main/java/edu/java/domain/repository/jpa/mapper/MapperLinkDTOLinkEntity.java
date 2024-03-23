package edu.java.domain.repository.jpa.mapper;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.jpa.entity.LinkEntity;
import java.net.URI;
import java.time.ZoneOffset;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapperLinkDTOLinkEntity {
    public static LinkDTO entityToDto(LinkEntity entity) {
        var created = entity.getCreatedAt();
        var update = entity.getLastUpdate();

        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setTgChatId(entity.getLinkChats().stream().findFirst().get().getChat().getChatId());
        linkDTO.setUri(URI.create(entity.getUri()));
        linkDTO.setLinkId(entity.getLinkId());
        if (created != null) {
            linkDTO.setCreatedAt(created.atOffset(ZoneOffset.UTC));
        }
        if (update != null) {
            linkDTO.setLastUpdate(update.atOffset(ZoneOffset.UTC));
        }

        return linkDTO;
    }

    public static LinkEntity dtoToEntity(LinkDTO dto) {
        var created = dto.getCreatedAt();
        var update = dto.getLastUpdate();

        LinkEntity linkEntity = new LinkEntity();
        linkEntity.setUri(dto.getUri().toString());
        linkEntity.setLinkId(dto.getLinkId());

        if (created != null) {
            linkEntity.setCreatedAt(created.toLocalDateTime());
        }
        if (update != null) {
            linkEntity.setLastUpdate(update.toLocalDateTime());
        }

        return linkEntity;
    }
}
