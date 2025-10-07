create table one_time_tokens
(
    token_value varchar(36) not null primary key,
    username    varchar(50) not null,
    expires_at  timestamp   not null
);