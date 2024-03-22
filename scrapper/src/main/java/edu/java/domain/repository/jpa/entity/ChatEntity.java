package edu.java.domain.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
@Table(name = "chat")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatEntity {
    @Id
    @Column(name = "chat_id")
    private Long chatId;
    private Boolean active;
    @OneToMany(mappedBy = "chat")
    private Set<LinkChatEntity> linkChats;
}
