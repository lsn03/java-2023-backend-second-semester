/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq.tables.pojos;


import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.Nullable;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class GithubCommit implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long commitId;
    private Long linkId;
    private String sha;
    private String author;
    private LocalDateTime createdAt;
    private String message;

    public GithubCommit() {}

    public GithubCommit(GithubCommit value) {
        this.commitId = value.commitId;
        this.linkId = value.linkId;
        this.sha = value.sha;
        this.author = value.author;
        this.createdAt = value.createdAt;
        this.message = value.message;
    }

    @ConstructorProperties({ "commitId", "linkId", "sha", "author", "createdAt", "message" })
    public GithubCommit(
        @Nullable Long commitId,
        @Nullable Long linkId,
        @Nullable String sha,
        @Nullable String author,
        @Nullable LocalDateTime createdAt,
        @Nullable String message
    ) {
        this.commitId = commitId;
        this.linkId = linkId;
        this.sha = sha;
        this.author = author;
        this.createdAt = createdAt;
        this.message = message;
    }

    /**
     * Getter for <code>GITHUB_COMMIT.COMMIT_ID</code>.
     */
    @Nullable
    public Long getCommitId() {
        return this.commitId;
    }

    /**
     * Setter for <code>GITHUB_COMMIT.COMMIT_ID</code>.
     */
    public void setCommitId(@Nullable Long commitId) {
        this.commitId = commitId;
    }

    /**
     * Getter for <code>GITHUB_COMMIT.LINK_ID</code>.
     */
    @Nullable
    public Long getLinkId() {
        return this.linkId;
    }

    /**
     * Setter for <code>GITHUB_COMMIT.LINK_ID</code>.
     */
    public void setLinkId(@Nullable Long linkId) {
        this.linkId = linkId;
    }

    /**
     * Getter for <code>GITHUB_COMMIT.SHA</code>.
     */
    @Size(max = 1000000000)
    @Nullable
    public String getSha() {
        return this.sha;
    }

    /**
     * Setter for <code>GITHUB_COMMIT.SHA</code>.
     */
    public void setSha(@Nullable String sha) {
        this.sha = sha;
    }

    /**
     * Getter for <code>GITHUB_COMMIT.AUTHOR</code>.
     */
    @Size(max = 1000000000)
    @Nullable
    public String getAuthor() {
        return this.author;
    }

    /**
     * Setter for <code>GITHUB_COMMIT.AUTHOR</code>.
     */
    public void setAuthor(@Nullable String author) {
        this.author = author;
    }

    /**
     * Getter for <code>GITHUB_COMMIT.CREATED_AT</code>.
     */
    @Nullable
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>GITHUB_COMMIT.CREATED_AT</code>.
     */
    public void setCreatedAt(@Nullable LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Getter for <code>GITHUB_COMMIT.MESSAGE</code>.
     */
    @Size(max = 1000000000)
    @Nullable
    public String getMessage() {
        return this.message;
    }

    /**
     * Setter for <code>GITHUB_COMMIT.MESSAGE</code>.
     */
    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final GithubCommit other = (GithubCommit) obj;
        if (this.commitId == null) {
            if (other.commitId != null)
                return false;
        }
        else if (!this.commitId.equals(other.commitId))
            return false;
        if (this.linkId == null) {
            if (other.linkId != null)
                return false;
        }
        else if (!this.linkId.equals(other.linkId))
            return false;
        if (this.sha == null) {
            if (other.sha != null)
                return false;
        }
        else if (!this.sha.equals(other.sha))
            return false;
        if (this.author == null) {
            if (other.author != null)
                return false;
        }
        else if (!this.author.equals(other.author))
            return false;
        if (this.createdAt == null) {
            if (other.createdAt != null)
                return false;
        }
        else if (!this.createdAt.equals(other.createdAt))
            return false;
        if (this.message == null) {
            if (other.message != null)
                return false;
        }
        else if (!this.message.equals(other.message))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.commitId == null) ? 0 : this.commitId.hashCode());
        result = prime * result + ((this.linkId == null) ? 0 : this.linkId.hashCode());
        result = prime * result + ((this.sha == null) ? 0 : this.sha.hashCode());
        result = prime * result + ((this.author == null) ? 0 : this.author.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.message == null) ? 0 : this.message.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("GithubCommit (");

        sb.append(commitId);
        sb.append(", ").append(linkId);
        sb.append(", ").append(sha);
        sb.append(", ").append(author);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(message);

        sb.append(")");
        return sb.toString();
    }
}
