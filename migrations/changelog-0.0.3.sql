-- liquibase formatted sql
--
-- changeset lsn03:1
-- comment add jooq style

drop table if exists chat cascade;
drop table if exists CHAT cascade;
drop table if exists link cascade;
drop table if exists LINK cascade;
drop table if exists link_chat cascade;
drop table if exists LINK_CHAT cascade;
drop table if exists site_type cascade;
drop table if exists SITE_TYPE cascade;
drop table if exists stackoverflow_answer cascade;
drop table if exists STACKOVERFLOW_ANSWER cascade;
drop table if exists github_commit cascade;
drop table if exists GITHUB_COMMIT cascade;

CREATE TABLE CHAT
(
    CHAT_ID BIGINT NOT NULL
        CONSTRAINT CHAT_PK
            PRIMARY KEY,
    ACTIVE  BOOLEAN DEFAULT TRUE
);

CREATE TABLE SITE_TYPE
(
    SITE_TYPE_ID SERIAL  NOT NULL
        PRIMARY KEY ,
    NAME         VARCHAR(50) NOT NULL
        UNIQUE

);

CREATE TABLE LINK
(
    LINK_ID      SERIAL
        CONSTRAINT LINK_PK
            PRIMARY KEY,
    URI          TEXT NOT NULL
        CONSTRAINT LINK_PK2
            UNIQUE,
    CREATED_AT   TIMESTAMP,
    LAST_UPDATE  TIMESTAMP,
    SITE_TYPE_ID BIGINT
        CONSTRAINT LINK_SITE_TYPE_SITE_TYPE_ID_FK
            REFERENCES SITE_TYPE

);

CREATE TABLE LINK_CHAT
(
    LINK_ID BIGINT
        CONSTRAINT LINK_TO_USER_LINK_LINK_ID_FK
            REFERENCES LINK,
    CHAT_ID BIGINT
        CONSTRAINT LINK_TO_USER_USER_USER_ID_FK
            REFERENCES CHAT,
    CONSTRAINT LINK_CHAT_PK
        UNIQUE (LINK_ID, CHAT_ID)
);

CREATE TABLE GITHUB_COMMIT
(
    COMMIT_ID  BIGINT NOT NULL
        PRIMARY KEY,
    LINK_ID    BIGINT
        REFERENCES LINK,
    SHA        TEXT
        CONSTRAINT GITHUB_COMMIT_PK
            UNIQUE,
    AUTHOR     TEXT,
    CREATED_AT TIMESTAMP,
    MESSAGE    TEXT

);

CREATE TABLE STACKOVERFLOW_ANSWER
(
    LINK_ID            BIGINT
        REFERENCES LINK,
    ANSWER_ID          BIGINT NOT NULL
        PRIMARY KEY,
    USER_NAME          TEXT,
    IS_ACCEPTED        BOOLEAN,
    CREATION_DATE      TIMESTAMP,
    LAST_ACTIVITY_DATE TIMESTAMP,
    LAST_EDIT_DATE     TIMESTAMP

);



INSERT INTO SITE_TYPE (NAME)
VALUES ('github'),
       ('stackoverflow');

