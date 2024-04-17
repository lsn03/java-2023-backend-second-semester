package edu.java.domain.repository.jpa.mapper;

import edu.java.domain.model.ChatDto;
import edu.java.domain.repository.jpa.entity.ChatEntity;
import java.util.Set;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapperChatDTOChatEntity {
    public static ChatDto entityToDto(ChatEntity entity) {
        return new ChatDto(entity.getChatId(), entity.getActive());
    }

    public static ChatEntity dtoToEntity(ChatDto dto) {
        return new ChatEntity(dto.getChatId(), dto.isActive(), Set.of());

    }
}
