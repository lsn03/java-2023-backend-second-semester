package edu.java.domain.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "github_commit",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"link_id", "sha"})}
)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GitHubCommitEntity {
    @Id
    @Column(name = "commit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commitId;
    @Column(name = "link_id")
    private Long linkId;
    @Column(nullable = false)
    private String sha;
    private String author;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    private String message;

    @ManyToOne
    @JoinColumn(name = "link_id")
    private LinkEntity linkEntity;
}
