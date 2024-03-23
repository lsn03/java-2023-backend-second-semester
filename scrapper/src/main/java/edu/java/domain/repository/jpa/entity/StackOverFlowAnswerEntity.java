package edu.java.domain.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stackoverflow_answer")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StackOverFlowAnswerEntity {

    @Id
    @Column(name = "answer_id")
    private Long answerId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "is_accepted")
    private Boolean isAccepted;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "last_activity_date")
    private LocalDateTime lastActivityDate;
    @Column(name = "last_edit_date")
    private LocalDateTime lastEditDate;

    @ManyToOne
    @JoinColumn(name = "link_id")
    private LinkEntity linkEntity;
}
