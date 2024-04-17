package edu.java.domain.repository.jpa.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkChatId implements Serializable {
    private Long link;
    private Long chat;

}
