create table document_request(
    id uuid not null primary key,
    first_name varchar not null,
    last_name varchar not null,
    number varchar not null,
    status varchar,
    created timestamp with time zone not null default current_timestamp,
    updated timestamp with time zone
)