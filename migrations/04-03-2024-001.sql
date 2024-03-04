drop table if exists chat;
drop table if exists link;
drop table if exists link_chat;

create table if not exists chat
(
    chat_id bigint not null
        constraint chat_pk
            primary key,
    active  boolean default true
);

create table if not exists link
(
    link_id     integer generated always as identity
        constraint link_pk
            primary key,
    uri         text not null
        constraint link_pk2
            unique,
    created_at  text,
    last_update text
);

create table if not exists link_chat
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
