package edu.java.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class LinkChatDTO {

    private Long linkId;
    private Long chatId;

    public LinkChatDTO(LinkDTO link, ChatDTO chat) {
        this.linkId = link.getLinkId();
        this.chatId = chat.getChatId();
    }
}
