/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq.tables.records;


import edu.java.domain.jooq.tables.GithubCommit;

import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


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
public class GithubCommitRecord extends UpdatableRecordImpl<GithubCommitRecord> implements Record6<Long, Long, String, String, LocalDateTime, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>GITHUB_COMMIT.COMMIT_ID</code>.
     */
    public void setCommitId(@Nullable Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>GITHUB_COMMIT.COMMIT_ID</code>.
     */
    @Nullable
    public Long getCommitId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>GITHUB_COMMIT.LINK_ID</code>.
     */
    public void setLinkId(@NotNull Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>GITHUB_COMMIT.LINK_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getLinkId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>GITHUB_COMMIT.SHA</code>.
     */
    public void setSha(@NotNull String value) {
        set(2, value);
    }

    /**
     * Getter for <code>GITHUB_COMMIT.SHA</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getSha() {
        return (String) get(2);
    }

    /**
     * Setter for <code>GITHUB_COMMIT.AUTHOR</code>.
     */
    public void setAuthor(@Nullable String value) {
        set(3, value);
    }

    /**
     * Getter for <code>GITHUB_COMMIT.AUTHOR</code>.
     */
    @Size(max = 1000000000)
    @Nullable
    public String getAuthor() {
        return (String) get(3);
    }

    /**
     * Setter for <code>GITHUB_COMMIT.CREATED_AT</code>.
     */
    public void setCreatedAt(@Nullable LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>GITHUB_COMMIT.CREATED_AT</code>.
     */
    @Nullable
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(4);
    }

    /**
     * Setter for <code>GITHUB_COMMIT.MESSAGE</code>.
     */
    public void setMessage(@Nullable String value) {
        set(5, value);
    }

    /**
     * Getter for <code>GITHUB_COMMIT.MESSAGE</code>.
     */
    @Size(max = 1000000000)
    @Nullable
    public String getMessage() {
        return (String) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row6<Long, Long, String, String, LocalDateTime, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row6<Long, Long, String, String, LocalDateTime, String> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return GithubCommit.GITHUB_COMMIT.COMMIT_ID;
    }

    @Override
    @NotNull
    public Field<Long> field2() {
        return GithubCommit.GITHUB_COMMIT.LINK_ID;
    }

    @Override
    @NotNull
    public Field<String> field3() {
        return GithubCommit.GITHUB_COMMIT.SHA;
    }

    @Override
    @NotNull
    public Field<String> field4() {
        return GithubCommit.GITHUB_COMMIT.AUTHOR;
    }

    @Override
    @NotNull
    public Field<LocalDateTime> field5() {
        return GithubCommit.GITHUB_COMMIT.CREATED_AT;
    }

    @Override
    @NotNull
    public Field<String> field6() {
        return GithubCommit.GITHUB_COMMIT.MESSAGE;
    }

    @Override
    @Nullable
    public Long component1() {
        return getCommitId();
    }

    @Override
    @NotNull
    public Long component2() {
        return getLinkId();
    }

    @Override
    @NotNull
    public String component3() {
        return getSha();
    }

    @Override
    @Nullable
    public String component4() {
        return getAuthor();
    }

    @Override
    @Nullable
    public LocalDateTime component5() {
        return getCreatedAt();
    }

    @Override
    @Nullable
    public String component6() {
        return getMessage();
    }

    @Override
    @Nullable
    public Long value1() {
        return getCommitId();
    }

    @Override
    @NotNull
    public Long value2() {
        return getLinkId();
    }

    @Override
    @NotNull
    public String value3() {
        return getSha();
    }

    @Override
    @Nullable
    public String value4() {
        return getAuthor();
    }

    @Override
    @Nullable
    public LocalDateTime value5() {
        return getCreatedAt();
    }

    @Override
    @Nullable
    public String value6() {
        return getMessage();
    }

    @Override
    @NotNull
    public GithubCommitRecord value1(@Nullable Long value) {
        setCommitId(value);
        return this;
    }

    @Override
    @NotNull
    public GithubCommitRecord value2(@NotNull Long value) {
        setLinkId(value);
        return this;
    }

    @Override
    @NotNull
    public GithubCommitRecord value3(@NotNull String value) {
        setSha(value);
        return this;
    }

    @Override
    @NotNull
    public GithubCommitRecord value4(@Nullable String value) {
        setAuthor(value);
        return this;
    }

    @Override
    @NotNull
    public GithubCommitRecord value5(@Nullable LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    @NotNull
    public GithubCommitRecord value6(@Nullable String value) {
        setMessage(value);
        return this;
    }

    @Override
    @NotNull
    public GithubCommitRecord values(@Nullable Long value1, @NotNull Long value2, @NotNull String value3, @Nullable String value4, @Nullable LocalDateTime value5, @Nullable String value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached GithubCommitRecord
     */
    public GithubCommitRecord() {
        super(GithubCommit.GITHUB_COMMIT);
    }

    /**
     * Create a detached, initialised GithubCommitRecord
     */
    @ConstructorProperties({ "commitId", "linkId", "sha", "author", "createdAt", "message" })
    public GithubCommitRecord(@Nullable Long commitId, @NotNull Long linkId, @NotNull String sha, @Nullable String author, @Nullable LocalDateTime createdAt, @Nullable String message) {
        super(GithubCommit.GITHUB_COMMIT);

        setCommitId(commitId);
        setLinkId(linkId);
        setSha(sha);
        setAuthor(author);
        setCreatedAt(createdAt);
        setMessage(message);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised GithubCommitRecord
     */
    public GithubCommitRecord(edu.java.domain.jooq.tables.pojos.GithubCommit value) {
        super(GithubCommit.GITHUB_COMMIT);

        if (value != null) {
            setCommitId(value.getCommitId());
            setLinkId(value.getLinkId());
            setSha(value.getSha());
            setAuthor(value.getAuthor());
            setCreatedAt(value.getCreatedAt());
            setMessage(value.getMessage());
            resetChangedOnNotNull();
        }
    }
}
