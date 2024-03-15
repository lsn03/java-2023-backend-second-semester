/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq.tables.records;


import edu.java.domain.jooq.tables.pojos.Chat;

import java.beans.ConstructorProperties;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
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
public class ChatRecord extends UpdatableRecordImpl<ChatRecord> implements Record2<Long, Boolean> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>CHAT.CHAT_ID</code>.
     */
    public void setChatId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>CHAT.CHAT_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getChatId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>CHAT.ACTIVE</code>.
     */
    public void setActive(@Nullable Boolean value) {
        set(1, value);
    }

    /**
     * Getter for <code>CHAT.ACTIVE</code>.
     */
    @Nullable
    public Boolean getActive() {
        return (Boolean) get(1);
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
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row2<Long, Boolean> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row2<Long, Boolean> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return edu.java.domain.jooq.tables.Chat.CHAT.CHAT_ID;
    }

    @Override
    @NotNull
    public Field<Boolean> field2() {
        return edu.java.domain.jooq.tables.Chat.CHAT.ACTIVE;
    }

    @Override
    @NotNull
    public Long component1() {
        return getChatId();
    }

    @Override
    @Nullable
    public Boolean component2() {
        return getActive();
    }

    @Override
    @NotNull
    public Long value1() {
        return getChatId();
    }

    @Override
    @Nullable
    public Boolean value2() {
        return getActive();
    }

    @Override
    @NotNull
    public ChatRecord value1(@NotNull Long value) {
        setChatId(value);
        return this;
    }

    @Override
    @NotNull
    public ChatRecord value2(@Nullable Boolean value) {
        setActive(value);
        return this;
    }

    @Override
    @NotNull
    public ChatRecord values(@NotNull Long value1, @Nullable Boolean value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ChatRecord
     */
    public ChatRecord() {
        super(edu.java.domain.jooq.tables.Chat.CHAT);
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    @ConstructorProperties({ "chatId", "active" })
    public ChatRecord(@NotNull Long chatId, @Nullable Boolean active) {
        super(edu.java.domain.jooq.tables.Chat.CHAT);

        setChatId(chatId);
        setActive(active);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    public ChatRecord(Chat value) {
        super(edu.java.domain.jooq.tables.Chat.CHAT);

        if (value != null) {
            setChatId(value.getChatId());
            setActive(value.getActive());
            resetChangedOnNotNull();
        }
    }
}
