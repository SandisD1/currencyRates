--liquibase formatted sql

--changeset sandis:1

CREATE TABLE currency
(
    entry_id      serial PRIMARY KEY,
    entry_date    timestamp NOT NULL,
    currency_code text      NOT NULL,
    currency_rate text      NOT NULL,
    timezone      text      NOT NULL
)