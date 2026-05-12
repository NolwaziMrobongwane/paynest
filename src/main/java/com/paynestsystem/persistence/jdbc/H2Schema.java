package com.paynestsystem.persistence.jdbc;

/**
 * DDL placeholders for H2 ({@code jdbc:h2:file:./data/paynest} — Capstone 4).
 * Students implement tables matching {@link com.paynestsystem.domain.TransactionRecord} and idempotency keys.
 */
public final class H2Schema {

    /**
     * Example starting point — replace with real columns and indexes.
     */
    public static final String CREATE_TRANSACTION_RECORDS = """
            -- TODO: CREATE TABLE transaction_records (
            --   id VARCHAR PRIMARY KEY,
            --   idempotency_key VARCHAR NOT NULL UNIQUE,
            --   ...
            -- );
            """;

    private H2Schema() {
    }
}
