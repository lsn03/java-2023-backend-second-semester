package edu.java.domain.repository.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "link_chat")
@IdClass(LinkChatId.class)
@NoArgsConstructor
@AllArgsConstructor
public class LinkChatEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "link_id", referencedColumnName = "link_id")
    private LinkEntity link;

    @Id
    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "chat_id")
    private ChatEntity chat;
}
