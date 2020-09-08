CREATE TABLE history
(
    id                  BIGSERIAL PRIMARY KEY,
    component_name      VARCHAR(100),
    old_status_value    VARCHAR(16),
    old_status_messages TEXT,
    new_status_value    VARCHAR(16),
    new_status_messages TEXT,
    timestamp           BIGINT
)
