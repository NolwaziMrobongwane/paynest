package com.paynestsystem.persistence;

import com.paynestsystem.domain.TransactionRecord;

import java.util.Optional;

/**
 * Persists {@link TransactionRecord} outside the JVM heap (Capstone 4 — implement with JDBC/H2, files, etc.).
 */
public interface TransactionRecordStore {

    void save(TransactionRecord record);

    Optional<TransactionRecord> findById(String id);
}
