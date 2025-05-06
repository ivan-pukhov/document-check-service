create table document_request(
    id bigserial primary key,
    source_service_name varchar,
    request_id uuid not null unique,
    first_name varchar,
    last_name varchar,
    number varchar,
    status varchar,
    document_id bigint,
    created timestamp with time zone not null default current_timestamp,
    updated timestamp with time zone
);

create table government_service_request(
    id bigserial primary key,
    document_request_id bigint,
    request varchar,
    response varchar,
    created timestamp with time zone not null default current_timestamp,
    updated timestamp with time zone,
    constraint government_service_request_document_request_id_fk foreign key (document_request_id)
        references document_request(id)
);

