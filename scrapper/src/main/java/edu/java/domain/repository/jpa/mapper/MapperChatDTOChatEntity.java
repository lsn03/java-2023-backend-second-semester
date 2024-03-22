package edu.java.domain.repository.jpa.mapper;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.jpa.entity.ChatEntity;
import java.util.Set;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapperChatDTOChatEntity {
    public static ChatDTO entityToDto(ChatEntity entity) {
        return new ChatDTO(entity.getChatId(), entity.getActive());
    }

    public static ChatEntity dtoToEntity(ChatDTO dto) {
        return new ChatEntity(dto.getChatId(), dto.isActive(), Set.of());

    }
}
