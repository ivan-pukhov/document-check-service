create table document(
    id bigserial primary key,
    first_name varchar,
    last_name varchar,
    number varchar,
    status varchar,
    created timestamp with time zone not null default current_timestamp,
    updated timestamp with time zone
);

create table document_check_request(
    id bigserial primary key,
    source_service_id varchar,
    request_id uuid not null,
    document_id bigint,
    created timestamp with time zone not null default current_timestamp,
    updated timestamp with time zone,
    constraint document_check_request_document_id_fk foreign key (document_id)
        references document(id)
);

