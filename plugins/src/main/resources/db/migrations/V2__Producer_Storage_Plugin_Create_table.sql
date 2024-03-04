CREATE TABLE producer_storage_1m
(
    id                  VARCHAR(100) PRIMARY KEY,
    producer_id         VARCHAR(100),
    tt                  VARCHAR(30),
    last                VARCHAR(30),
    min                 VARCHAR(30),
    max                 VARCHAR(30),
    avg                 VARCHAR(30),
    err                 VARCHAR(30),
    mcr                 VARCHAR(30),
    tr                  VARCHAR(30),
    cr                  VARCHAR(30),
    timestamp           BIGINT
);
CREATE INDEX producer_storage_1m_id_idx ON producer_storage_1m(id);
CREATE INDEX producer_storage_1m_producer_id_idx ON producer_storage_1m(producer_id);
CREATE INDEX producer_storage_1m_timestamp_idx ON producer_storage_1m(timestamp);

CREATE TABLE producer_storage_5m
(
    id                  VARCHAR(100) PRIMARY KEY,
    producer_id         VARCHAR(100),
    tt                  VARCHAR(30),
    last                VARCHAR(30),
    min                 VARCHAR(30),
    max                 VARCHAR(30),
    avg                 VARCHAR(30),
    err                 VARCHAR(30),
    mcr                 VARCHAR(30),
    tr                  VARCHAR(30),
    cr                  VARCHAR(30),
    timestamp           BIGINT
);
CREATE INDEX producer_storage_5m_id_idx ON producer_storage_5m(id);
CREATE INDEX producer_storage_5m_producer_id_idx ON producer_storage_5m(producer_id);
CREATE INDEX producer_storage_5m_timestamp_idx ON producer_storage_5m(timestamp);

CREATE TABLE producer_storage_15m
(
    id                  VARCHAR(100) PRIMARY KEY,
    producer_id         VARCHAR(100),
    tt                  VARCHAR(30),
    last                VARCHAR(30),
    min                 VARCHAR(30),
    max                 VARCHAR(30),
    avg                 VARCHAR(30),
    err                 VARCHAR(30),
    mcr                 VARCHAR(30),
    tr                  VARCHAR(30),
    cr                  VARCHAR(30),
    timestamp           BIGINT
);
CREATE INDEX producer_storage_15m_id_idx ON producer_storage_15m(id);
CREATE INDEX producer_storage_15m_producer_id_idx ON producer_storage_15m(producer_id);
CREATE INDEX producer_storage_15m_timestamp_idx ON producer_storage_15m(timestamp);

CREATE TABLE producer_storage_1h
(
    id                  VARCHAR(100) PRIMARY KEY,
    producer_id         VARCHAR(100),
    tt                  VARCHAR(30),
    last                VARCHAR(30),
    min                 VARCHAR(30),
    max                 VARCHAR(30),
    avg                 VARCHAR(30),
    err                 VARCHAR(30),
    mcr                 VARCHAR(30),
    tr                  VARCHAR(30),
    cr                  VARCHAR(30),
    timestamp           BIGINT
);
CREATE INDEX producer_storage_1h_id_idx ON producer_storage_1h(id);
CREATE INDEX producer_storage_1h_producer_id_idx ON producer_storage_1h(producer_id);
CREATE INDEX producer_storage_1h_timestamp_idx ON producer_storage_1h(timestamp);
