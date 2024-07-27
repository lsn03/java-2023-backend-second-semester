package edu.java.domain.repository.jpa.mapper;

import edu.java.domain.model.StackOverFlowAnswerDto;
import edu.java.domain.repository.jpa.entity.LinkEntity;
import edu.java.domain.repository.jpa.entity.StackOverFlowAnswerEntity;
import java.time.ZoneOffset;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapperStackOverFlowDTOEntity {
    public static StackOverFlowAnswerDto entityToDto(StackOverFlowAnswerEntity entity) {
        var lastActivity = entity.getLastActivityDate();
        var lastEdit = entity.getLastEditDate();

        return new StackOverFlowAnswerDto(
            entity.getLinkEntity().getLinkId(),
            entity.getAnswerId(),
            entity.getUserName(),
            entity.getIsAccepted(),
            entity.getCreationDate().atOffset(ZoneOffset.UTC),
            lastActivity == null ? null : lastActivity.atOffset(ZoneOffset.UTC),
            lastEdit == null ? null : lastEdit.atOffset(ZoneOffset.UTC)
        );
    }

    public static StackOverFlowAnswerEntity dtoToEntity(StackOverFlowAnswerDto dto) {
        var lastActivity = dto.getLastActivityDate();
        var lastEdit = dto.getLastEditDate();
        var entity = new LinkEntity();
        entity.setLinkId(dto.getLinkId());
        return new StackOverFlowAnswerEntity(

            dto.getAnswerId(),
            dto.getUserName(),
            dto.getIsAccepted(),
            dto.getCreationDate().toLocalDateTime(),
            lastActivity == null ? null : lastActivity.toLocalDateTime(),
            lastEdit == null ? null : lastEdit.toLocalDateTime(),
            entity

        );
    }
}
