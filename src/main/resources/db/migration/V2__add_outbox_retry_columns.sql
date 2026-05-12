-- V2__add_outbox_retry_columns.sql
ALTER TABLE outbox_events ADD COLUMN retry_count INTEGER NOT NULL DEFAULT 0;
ALTER TABLE outbox_events ADD COLUMN dead BOOLEAN NOT NULL DEFAULT FALSE;
