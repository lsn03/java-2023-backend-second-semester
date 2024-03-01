--liquibase formatted sql

--changeset lsn03:1
--comment first migration


create table chat
(
    chat_id bigint not null
        constraint chat_pk
            primary key,
    active  boolean default false
);

create table link
(
    link_id integer not null
        constraint link_pk
            primary key,
    uri     text    not null
);

create table link_chat
(
    link_id integer
        constraint link_to_user_link_link_id_fk
            references link,
    chat_id bigint
        constraint link_to_user_user_user_id_fk
            references chat
);


insert into chat (chat_id, active)
values (1,false);

insert into chat (chat_id, active)
values (2,true);
