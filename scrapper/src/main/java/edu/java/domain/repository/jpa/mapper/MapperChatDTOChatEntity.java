package edu.java.domain.repository.jpa.mapper;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.repository.jpa.entity.ChatEntity;
import java.util.Set;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapperChatDTOChatEntity {
    public static ChatDTO chatEntitytoChatDTO(ChatEntity chatEntity) {
        return new ChatDTO(chatEntity.getChatId(), chatEntity.getActive());
    }

    public static ChatEntity chatDTOtoChatEntity(ChatDTO chatDTO) {
        return new ChatEntity(chatDTO.getChatId(), chatDTO.isActive(), Set.of());

    }
}
