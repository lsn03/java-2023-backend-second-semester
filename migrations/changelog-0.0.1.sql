-- liquibase formatted sql
--
-- changeset lsn03:1
-- comment second migration

drop table if exists chat cascade;
drop table if exists link cascade;
drop table if exists link_chat cascade;

create table chat
(
    chat_id bigint not null
        constraint chat_pk
            primary key,
    active  boolean default true
);

create table link
(
    link_id     integer generated always as identity
        constraint link_pk
            primary key,
    uri         text not null
        constraint link_pk2
            unique,
    hash        text,
    created_at  timestamp,
    last_update timestamp
);

create table link_chat
(
    link_id integer
        constraint link_to_user_link_link_id_fk
            references link,
    chat_id bigint
        constraint link_to_user_user_user_id_fk
            references chat,
    constraint link_chat_pk
        unique (link_id, chat_id)
);

