-- liquibase formatted sql
--
-- changeset lsn03:1
-- comment third migration add site type, hashes

drop table if exists chat cascade;
drop table if exists link cascade;
drop table if exists link_chat cascade;
drop table if exists site_type cascade;
drop table if exists stackoverflow_answer cascade;
drop table if exists github_commit cascade;

create table chat
(
    chat_id bigint not null
        constraint chat_pk
            primary key,
    active  boolean default true
);

create table site_type
(
    site_type_id integer     not null GENERATED ALWAYS AS identity
        primary key,
    name         varchar(50) not null
        unique
);

create table link
(
    link_id      integer generated always as identity
        constraint link_pk
            primary key,
    uri          text not null
        constraint link_pk2
            unique,
    created_at   timestamp,
    last_update  timestamp,
    site_type_id integer
        constraint link_site_type_site_type_id_fk
            references site_type
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

create table github_commit
(
    commit_id  integer not null
        primary key,
    link_id    integer
        references link,
    sha        text
        constraint github_commit_pk
            unique,
    author     text,
    created_at timestamp,
    message    text
);

create table stackoverflow_answer
(
    link_id            integer
        references link,
    answer_id          integer not null
        primary key,
    user_name          text,
    is_accepted        boolean,
    creation_date      timestamp,
    last_activity_date timestamp,
    last_edit_date     timestamp
);



insert into site_type (name)
values ('github'),
       ('stackoverflow');

